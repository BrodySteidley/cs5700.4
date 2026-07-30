package computer.cpu.instruction

import computer.cpu.D5700CPUMemoryAccess

internal class SkipNotEqualInstruction(cpuMemory : D5700CPUMemoryAccess) : SkipEqualInstruction(cpuMemory)
{
	override fun performInstruction(parameters : Array<Int>)
	{
		skip = cpuMemory.registers[parameters[0]] != cpuMemory.registers[parameters[1]]
	}
}

