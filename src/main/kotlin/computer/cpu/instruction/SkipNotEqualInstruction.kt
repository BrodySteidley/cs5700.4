package computer.cpu.instruction

import computer.cpu.D5700CPUMemoryAccess

internal class SkipNotEqualInstruction(descriptor : Short, cpuMemory : D5700CPUMemoryAccess) : SkipEqualInstruction(descriptor, cpuMemory)
{
	override fun performInstruction(parameters : Array<Int>)
	{
		skip = cpuMemory.registers[parameters[0]] != cpuMemory.registers[parameters[1]]
	}
}

