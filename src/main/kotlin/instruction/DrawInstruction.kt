package instruction

import component.D5700CPURegisters

class DrawInstruction(cpuMemory : D5700CPURegisters) : D5700Instruction(cpuMemory)
{
	override fun performInstruction(parameters : Array<Int>)
	{
		val addr = (parameters[2] + parameters[1] * 8).toUShort()
		cpuMemory.writeToScreen(addr, cpuMemory.registers[parameters[0]])
	}
}

