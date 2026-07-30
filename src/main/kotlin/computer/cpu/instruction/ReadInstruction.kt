package computer.cpu.instruction

import computer.cpu.D5700CPUMemoryAccess
import exception.InstructionParameterException

internal class ReadInstruction(descriptor : Short, cpuMemory : D5700CPUMemoryAccess) : D5700Instruction(descriptor, cpuMemory)
{
	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		if ((descriptor.toInt() and 0xFF) != 0)
			throw InstructionParameterException("READ")
		return arrayOf<Int>( (descriptor.toInt() shr 8 and 0xF))
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		cpuMemory.registers[parameters[0]] = cpuMemory.readSelectedMemory()
	}
}

