package computer.cpu.instruction

import computer.cpu.D5700CPURegisterAccess
import exception.InstructionParameterException

internal class SetTInstruction(cpuMemory : D5700CPURegisterAccess) : D5700Instruction(cpuMemory)
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

