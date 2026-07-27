package instruction

import component.D5700CPURegisters

class SkipNotEqualInstruction(cpuMemory : D5700CPURegisters) : SkipEqualInstruction(cpuMemory)
{
	override fun performInstruction(parameters : Array<Int>)
	{
		skip = cpuMemory.registers[parameters[0]] != cpuMemory.registers[parameters[1]]
	}
}

