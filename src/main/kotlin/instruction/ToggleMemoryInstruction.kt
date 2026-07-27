package instruction

import component.D5700CPURegisters
import exception.InstructionParameterException

class ToggleMemoryInstruction(cpuMemory : D5700CPURegisters) : D5700Instruction(cpuMemory)
{
	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		if ((descriptor.toInt() and 0xFFF) != 0)
			throw InstructionParameterException("TOGGLE_MEMORY")
		return arrayOf<Int>()
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		cpuMemory.memory = !cpuMemory.memory
	}
}

