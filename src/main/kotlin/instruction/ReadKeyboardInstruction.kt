package instruction

import component.D5700CPURegisters
import exception.InstructionParameterException

class ReadKeyboardInstruction(cpuMemory : D5700CPURegisters) : D5700Instruction(cpuMemory)
{
	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		if ((descriptor.toInt() and 0xFF) != 0)
			throw InstructionParameterException("READ_KEYBOARD")

		return arrayOf<Int>(
			(descriptor.toInt() shr 8 and 0xF),
		)
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		cpuMemory.registers[parameters[0]] = cpuMemory.readD7500Input()
	}
}
