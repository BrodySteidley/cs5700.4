package instruction

import component.D5700CPURegisters

class SubInstruction(cpuMemory : D5700CPURegisters) : D5700Instruction(cpuMemory)
{
	override fun performInstruction(parameters : Array<Int>)
	{
		cpuMemory.registers[parameters[2]] = (cpuMemory.registers[parameters[0]] - cpuMemory.registers[parameters[1]]).toByte()
	}
}

