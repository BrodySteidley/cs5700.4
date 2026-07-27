package instruction

import component.D5700CPURegisters
import exception.InstructionParameterException

class SetTInstruction(cpuMemory : D5700CPURegisters) : D5700Instruction(cpuMemory)
{
	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		if ((descriptor.toInt() and 0xF) != 0)
			throw InstructionParameterException("SET_T")
		return arrayOf<Int>(
			(descriptor.toInt() shr 4 and 0xFF)
		)
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		cpuMemory.timer = parameters[0].toByte()
	}
}

