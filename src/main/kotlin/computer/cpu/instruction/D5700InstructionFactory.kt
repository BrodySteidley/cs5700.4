package computer.cpu.instruction

import computer.cpu.D5700CPUMemoryAccess

internal object D5700InstructionFactory {
    private val instructionMap = mapOf<Int, (Short, D5700CPUMemoryAccess) -> D5700Instruction> (
        0x0 to ::StoreInstruction,
        0x1 to ::AddInstruction,
        0x2 to ::SubInstruction,
        0x3 to ::ReadInstruction,
        0x4 to ::WriteInstruction,
        0x5 to ::JumpInstruction,
        0x6 to ::ReadKeyboardInstruction,
        0x7 to ::ToggleMemoryInstruction,
        0x8 to ::SkipEqualInstruction,
        0x9 to ::SkipNotEqualInstruction,
        0xA to ::SetAInstruction,
        0xB to ::SetTInstruction,
        0xC to ::ReadTInstruction,
        0xD to ::ConvertToBaseTenInstruction,
        0xE to ::ConvertByteToAsciiInstruction,
        0xF to ::DrawInstruction,
    )

    fun createInstructionFromDescriptor(descriptor : Short, cpuMemory : D5700CPUMemoryAccess) : D5700Instruction
    {
        val nybble = (descriptor.toInt() shr 12) and 0xF
        val concreteInstruction = instructionMap[nybble]!!.invoke(descriptor, cpuMemory)
        return concreteInstruction
    }

    fun performInstructionDescriptor(descriptor : Short, cpuMemory : D5700CPUMemoryAccess)
    {
        createInstructionFromDescriptor(descriptor, cpuMemory).perform()
    }
}