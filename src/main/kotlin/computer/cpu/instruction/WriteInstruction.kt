package computer.cpu.instruction

import computer.cpu.D5700CPURegisterAccess
import exception.InstructionParameterException

internal class WriteInstruction(cpuMemory : D5700CPURegisterAccess) : D5700Instruction(cpuMemory)
{
	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		if ((descriptor.toInt() and 0xFF) != 0)
			throw InstructionParameterException("WRITE")
		return arrayOf<Int>( (descriptor.toInt() shr 8 and 0xF))
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		cpuMemory.writeSelectedMemory(cpuMemory.registers[parameters[0]])
	}
}

