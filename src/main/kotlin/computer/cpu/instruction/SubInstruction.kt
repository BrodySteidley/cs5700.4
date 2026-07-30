package computer.cpu.instruction

import computer.cpu.D5700CPUMemoryAccess

internal class SubInstruction(descriptor : Short, cpuMemory : D5700CPUMemoryAccess) : D5700Instruction(descriptor, cpuMemory)
{
	override fun performInstruction(parameters : Array<Int>)
	{
		cpuMemory.registers[parameters[2]] = (cpuMemory.registers[parameters[0]] - cpuMemory.registers[parameters[1]]).toByte()
	}
}

