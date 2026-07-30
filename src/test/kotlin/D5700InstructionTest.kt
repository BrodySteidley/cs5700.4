import component.D5700CPURegisters
import component.Memory
import component.MemoryIO
import instruction.*
import exception.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

private class FakeMemoryIO : Memory(ByteArray(500))

private class TestInstruction(
    cpu: D5700CPURegisters
) : D5700Instruction(cpu) {

    var receivedParameters: Array<Int>? = null

    override fun performInstruction(parameters: Array<Int>) {
        receivedParameters = parameters
    }

    fun split(descriptor: Short) = splitDescriptor(descriptor)

    fun increment() = incrementProgramCounter()
}

class D5700InstructionTest {

    private fun cpu() = D5700CPURegisters(
        FakeMemoryIO(),
        FakeMemoryIO(),
        FakeMemoryIO(),
        FakeMemoryIO()
    )

    @Test
    fun `splitDescriptor extracts three parameter nybbles`() {
        val instruction = TestInstruction(cpu())

        val params = instruction.split(0xABCD.toShort())

        assertArrayEquals(arrayOf(0xB, 0xC, 0xD), params)
    }

    @Test
    fun `incrementProgramCounter advances by two bytes`() {
        val cpu = cpu()
        val instruction = TestInstruction(cpu)

        cpu.programCounter = 10

        instruction.increment()

        assertEquals(12, cpu.programCounter)
    }

    @Test
    fun `incrementProgramCounter wraps like Short arithmetic`() {
        val cpu = cpu()
        val instruction = TestInstruction(cpu)

        cpu.programCounter = Short.MAX_VALUE

        instruction.increment()

        assertEquals((Short.MAX_VALUE + 2).toShort(), cpu.programCounter)
    }

    @Test
    fun `StoreInstruction stores immediate value into register`() {
        val cpu = cpu()

        D5700Instruction.perform(0x0123.toShort(), cpu)

        assertEquals(0x23.toByte(), cpu.registers[1])
        assertEquals(2, cpu.programCounter)

        D5700Instruction.perform(0x01FF.toShort(), cpu)

        assertEquals((-1).toByte(), cpu.registers[1])
    }

    @Test
    fun `AddInstruction adds two registers into destination register`() {
        val cpu = cpu()

        cpu.registers[1] = 10
        cpu.registers[2] = 20

        D5700Instruction.perform(0x1123, cpu)

        assertEquals(30.toByte(), cpu.registers[3])
    }

    @Test
    fun `SubInstruction subtracts two registers into destination register`() {
        val cpu = cpu()

        cpu.registers[1] = 20
        cpu.registers[2] = 10

        D5700Instruction.perform(0x2123.toShort(), cpu)

        assertEquals(10.toByte(), cpu.registers[3])
        assertEquals(2, cpu.programCounter)
    }

    @Test
    fun `ReadInstruction reads selected memory into register`() {
        val rom = FakeMemoryIO()
        val ram = FakeMemoryIO()

        rom.write(10u, 42)

        val cpu = D5700CPURegisters(
            rom,
            ram,
            FakeMemoryIO(),
            FakeMemoryIO()
        )

        cpu.memory = true
        cpu.address = 10

        D5700Instruction.perform(0x3100.toShort(), cpu)

        assertEquals(42.toByte(), cpu.registers[1])
        assertEquals(2, cpu.programCounter)
    }

    @Test
    fun `ReadInstruction reads from RAM when memory flag is false`() {
        val rom = FakeMemoryIO()
        val ram = FakeMemoryIO()

        ram.write(5u, 99)

        val cpu = D5700CPURegisters(
            rom,
            ram,
            FakeMemoryIO(),
            FakeMemoryIO()
        )

        cpu.memory = false
        cpu.address = 5

        D5700Instruction.perform(0x3200.toShort(), cpu)

        assertEquals(99.toByte(), cpu.registers[2])
    }

    @Test
    fun `WriteInstruction writes register value to selected memory`() {
        val rom = FakeMemoryIO()
        val ram = FakeMemoryIO()

        val cpu = D5700CPURegisters(
            rom,
            ram,
            FakeMemoryIO(),
            FakeMemoryIO()
        )

        cpu.memory = false
        cpu.address = 10
        cpu.registers[1] = 42

        D5700Instruction.perform(0x4100.toShort(), cpu)

        assertEquals(42.toByte(), ram.read(10u))
        assertEquals(2, cpu.programCounter)
    }

    @Test
    fun `WriteInstruction writes to ROM when memory flag is true`() {
        val rom = FakeMemoryIO()
        val ram = FakeMemoryIO()

        val cpu = D5700CPURegisters(
            rom,
            ram,
            FakeMemoryIO(),
            FakeMemoryIO()
        )

        cpu.memory = true
        cpu.address = 5
        cpu.registers[2] = 99

        D5700Instruction.perform(0x4200.toShort(), cpu)

        assertEquals(99.toByte(), rom.read(5u))
    }

    @Test
    fun `WriteInstruction throws when low byte is not zero`() {
        val cpu = cpu()

        assertThrows(InstructionParameterException::class.java) {
            D5700Instruction.perform(0x4101.toShort(), cpu)
        }
    }

    @Test
    fun `JumpInstruction sets program counter to jump location`() {
        val cpu = cpu()

        D5700Instruction.perform(0x5120.toShort(), cpu)

        assertEquals(0x120.toShort(), cpu.programCounter)
    }

    @Test
    fun `JumpInstruction throws for odd jump location`() {
        val cpu = cpu()

        assertThrows(InstructionException::class.java) {
            D5700Instruction.perform(0x5121.toShort(), cpu)
        }
    }

    @Test
    fun `JumpInstruction supports maximum even address`() {
        val cpu = cpu()

        D5700Instruction.perform(0x5FFE.toShort(), cpu)

        assertEquals(0xFFE.toShort(), cpu.programCounter)
    }

    @Test
    fun `ReadKeyboardInstruction stores input value into register`() {
        val input = FakeMemoryIO()

        input.write(0u, 0x41)

        val cpu = D5700CPURegisters(
            FakeMemoryIO(),
            FakeMemoryIO(),
            FakeMemoryIO(),
            input
        )

        D5700Instruction.perform(0x6100.toShort(), cpu)

        assertEquals(0x41.toByte(), cpu.registers[1])
        assertEquals(2, cpu.programCounter)
    }

    @Test
    fun `ReadKeyboardInstruction throws when low byte is not zero`() {
        val cpu = cpu()

        assertThrows(InstructionParameterException::class.java) {
            D5700Instruction.perform(0x6101.toShort(), cpu)
        }
    }

    @Test
    fun `ToggleMemoryInstruction toggles memory selection`() {
        val cpu = cpu()

        cpu.memory = false

        D5700Instruction.perform(0x7000.toShort(), cpu)

        assertTrue(cpu.memory)
        assertEquals(2, cpu.programCounter)

        D5700Instruction.perform(0x7000.toShort(), cpu)

        assertFalse(cpu.memory)
        assertEquals(4, cpu.programCounter)
    }

    @Test
    fun `ToggleMemoryInstruction throws when parameters are present`() {
        val cpu = cpu()

        assertThrows(InstructionParameterException::class.java) {
            D5700Instruction.perform(0x7001.toShort(), cpu)
        }
        assertThrows(InstructionParameterException::class.java) {
            D5700Instruction.perform(0x7010.toShort(), cpu)
        }
        assertThrows(InstructionParameterException::class.java) {
            D5700Instruction.perform(0x7100.toShort(), cpu)
        }
    }

    @Test
    fun `SkipEqualInstruction skips next instruction when registers are equal`() {
        val cpu = cpu()

        cpu.registers[1] = 42
        cpu.registers[2] = 42
        cpu.programCounter = 0

        D5700Instruction.perform(0x8120.toShort(), cpu)

        assertEquals(4, cpu.programCounter)
    }

    @Test
    fun `SkipEqualInstruction advances normally when registers are different`() {
        val cpu = cpu()

        cpu.registers[1] = 42
        cpu.registers[2] = 43
        cpu.programCounter = 0

        D5700Instruction.perform(0x8120.toShort(), cpu)

        assertEquals(2, cpu.programCounter)
    }

    @Test
    fun `SkipEqualInstruction throws when low nibble is not zero`() {
        val cpu = cpu()

        assertThrows(InstructionParameterException::class.java) {
            D5700Instruction.perform(0x8121.toShort(), cpu)
        }
    }

    @Test
    fun `SkipNotEqualInstruction skips next instruction when registers are different`() {
        val cpu = cpu()

        cpu.registers[1] = 42
        cpu.registers[2] = 43
        cpu.programCounter = 0

        D5700Instruction.perform(0x9120.toShort(), cpu)

        assertEquals(4, cpu.programCounter)
    }

    @Test
    fun `SkipNotEqualInstruction advances normally when registers are equal`() {
        val cpu = cpu()

        cpu.registers[1] = 42
        cpu.registers[2] = 42
        cpu.programCounter = 0

        D5700Instruction.perform(0x9120.toShort(), cpu)

        assertEquals(2, cpu.programCounter)
    }

    @Test
    fun `SkipNotEqualInstruction throws when low nibble is not zero`() {
        val cpu = cpu()

        assertThrows(InstructionParameterException::class.java) {
            D5700Instruction.perform(0x9121.toShort(), cpu)
        }
    }


    @Test
    fun `SetAInstruction sets memory address`() {
        val cpu = cpu()

        D5700Instruction.perform(0xA123.toShort(), cpu)

        assertEquals(0x123.toShort(), cpu.address)
        assertEquals(2, cpu.programCounter)
    }

    @Test
    fun `SetTInstruction sets timer value`() {
        val cpu = cpu()

        D5700Instruction.perform(0xB5A0.toShort(), cpu)

        assertEquals(0x5A.toByte(), cpu.timer)
        assertEquals(2, cpu.programCounter)
    }

    @Test
    fun `SetTInstruction throws when low nibble is not zero`() {
        val cpu = cpu()

        assertThrows(InstructionParameterException::class.java) {
            D5700Instruction.perform(0xB5A1.toShort(), cpu)
        }
    }

    @Test
    fun `ReadTInstruction copies timer value into register`() {
        val cpu = cpu()

        cpu.timer = 60

        D5700Instruction.perform(0xC300.toShort(), cpu)

        assertEquals(60.toByte(), cpu.registers[3])
        assertEquals(2, cpu.programCounter)
    }

    @Test
    fun `ReadTInstruction throws when low byte is not zero`() {
        val cpu = cpu()

        assertThrows(InstructionParameterException::class.java) {
            D5700Instruction.perform(0xC301.toShort(), cpu)
        }
        assertThrows(InstructionParameterException::class.java) {
            D5700Instruction.perform(0xC310.toShort(), cpu)
        }
    }

    @Test
    fun `ConvertToBaseTenInstruction writes decimal digits to memory`() {
        val ram = FakeMemoryIO()

        val cpu = D5700CPURegisters(
            FakeMemoryIO(),
            ram,
            FakeMemoryIO(),
            FakeMemoryIO()
        )

        cpu.memory = false
        cpu.address = 10
        cpu.registers[5] = 123

        D5700Instruction.perform(0xD050.toShort(), cpu)

        assertEquals(1.toByte(), ram.read(10u))
        assertEquals(2.toByte(), ram.read(11u))
        assertEquals(3.toByte(), ram.read(12u))

        assertEquals(10, cpu.address)
        assertEquals(2, cpu.programCounter)
    }

    @Test
    fun `ConvertToBaseTenInstruction throws when low nibble is not zero`() {
        val cpu = cpu()

        assertThrows(InstructionParameterException::class.java) {
            D5700Instruction.perform(0xD501.toShort(), cpu)
        }
    }

    @Test
    fun `ConvertByteToAsciiInstruction converts numeric value to ascii`() {
        val cpu = cpu()

        cpu.registers[1] = 5

        D5700Instruction.perform(0xE120.toShort(), cpu)

        assertEquals('5'.code.toByte(), cpu.registers[2])
        assertEquals(2, cpu.programCounter)
    }

    @Test
    fun `ConvertByteToAsciiInstruction throws when value is greater than F`() {
        val cpu = cpu()

        cpu.registers[1] = 16

        assertThrows(InstructionException::class.java) {
            D5700Instruction.perform(0xE120.toShort(), cpu)
        }
    }

    @Test
    fun `ConvertByteToAsciiInstruction throws when low nibble is not zero`() {
        val cpu = cpu()

        assertThrows(Exception::class.java) {
            D5700Instruction.perform(0xE121.toShort(), cpu)
        }
    }

    @Test
    fun `DrawInstruction writes register value to screen address`() {
        val screen = FakeMemoryIO()

        val cpu = D5700CPURegisters(
            FakeMemoryIO(),
            FakeMemoryIO(),
            screen,
            FakeMemoryIO()
        )

        cpu.registers[0] = 65

        D5700Instruction.perform(0xF012.toShort(), cpu)

        assertEquals(65.toByte(), screen.read(10u))
        assertEquals(2, cpu.programCounter)
    }
}