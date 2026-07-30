package computer
import computer.cpu.D5700CPURegisterAccess
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

private class FakeMemoryIO : MemoryIO {
    val memory = mutableMapOf<UShort, Byte>()

    var lastReadAddress: UShort? = null
    var lastWriteAddress: UShort? = null
    var lastWriteValue: Byte? = null

    override fun read(address: UShort): Byte {
        lastReadAddress = address
        return memory[address] ?: 0
    }

    override fun write(address: UShort, value: Byte) {
        lastWriteAddress = address
        lastWriteValue = value
        memory[address] = value
    }
}

class D5700CPURegisterAccessTest {

    @Test
    fun `writeSelectedMemory writes to RAM when memory flag is false`() {
        val rom = FakeMemoryIO()
        val ram = FakeMemoryIO()
        val screen = FakeMemoryIO()
        val input = FakeMemoryIO()

        val registers = D5700CPURegisterAccess(rom, ram, screen, input)

        registers.memory = false
        registers.address = 42
        registers.writeSelectedMemory(99)

        assertEquals(42.toUShort(), ram.lastWriteAddress)
        assertEquals(99.toByte(), ram.lastWriteValue)

        assertNull(rom.lastWriteAddress)
    }

    @Test
    fun `writeSelectedMemory writes to ROM when memory flag is true`() {
        val rom = FakeMemoryIO()
        val ram = FakeMemoryIO()
        val screen = FakeMemoryIO()
        val input = FakeMemoryIO()

        val registers = D5700CPURegisterAccess(rom, ram, screen, input)

        registers.memory = true
        registers.address = 12
        registers.writeSelectedMemory(7)

        assertEquals(12.toUShort(), rom.lastWriteAddress)
        assertEquals(7.toByte(), rom.lastWriteValue)

        assertNull(ram.lastWriteAddress)
    }

    @Test
    fun `readSelectedMemory reads from RAM when memory flag is false`() {
        val rom = FakeMemoryIO()
        val ram = FakeMemoryIO()
        val screen = FakeMemoryIO()
        val input = FakeMemoryIO()

        ram.memory[5u] = 42

        val registers = D5700CPURegisterAccess(rom, ram, screen, input)
        registers.memory = false
        registers.address = 5

        assertEquals(42.toByte(), registers.readSelectedMemory())
        assertEquals(5.toUShort(), ram.lastReadAddress)
        assertNull(rom.lastReadAddress)
    }

    @Test
    fun `readSelectedMemory reads from ROM when memory flag is true`() {
        val rom = FakeMemoryIO()
        val ram = FakeMemoryIO()
        val screen = FakeMemoryIO()
        val input = FakeMemoryIO()

        rom.memory[9u] = 123.toByte()

        val registers = D5700CPURegisterAccess(rom, ram, screen, input)
        registers.memory = true
        registers.address = 9

        assertEquals(123.toByte(), registers.readSelectedMemory())
        assertEquals(9.toUShort(), rom.lastReadAddress)
        assertNull(ram.lastReadAddress)
    }

    @Test
    fun `writeToScreen delegates to screen device`() {
        val registers = D5700CPURegisterAccess(
            FakeMemoryIO(),
            FakeMemoryIO(),
            FakeMemoryIO(),
            FakeMemoryIO()
        )

        val screen = registers.run {
            javaClass.getDeclaredField("screenIO").apply { isAccessible = true }
                .get(this) as FakeMemoryIO
        }

        registers.writeToScreen(17u, 'A'.code.toByte())

        assertEquals(17.toUShort(), screen.lastWriteAddress)
        assertEquals('A'.code.toByte(), screen.lastWriteValue)
    }

    @Test
    fun `readD7500Input reads from address zero`() {
        val input = FakeMemoryIO()
        input.memory[0u] = 0x5A.toByte()

        val registers = D5700CPURegisterAccess(
            FakeMemoryIO(),
            FakeMemoryIO(),
            FakeMemoryIO(),
            input
        )

        assertEquals(0x5A.toByte(), registers.readD7500Input())
        assertEquals(0.toUShort(), input.lastReadAddress)
    }

    @Test
    fun `registers are initialized correctly`() {
        val registers = D5700CPURegisterAccess(
            FakeMemoryIO(),
            FakeMemoryIO(),
            FakeMemoryIO(),
            FakeMemoryIO()
        )

        assertEquals(8, registers.registers.size)
        assertTrue(registers.registers.all { it == 0.toByte() })

        assertEquals(0, registers.programCounter)
        assertEquals(0.toByte(), registers.timer)
        assertEquals(0, registers.address)
        assertFalse(registers.memory)
    }
}