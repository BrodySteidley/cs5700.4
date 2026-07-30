package computer.cpu.instruction

import computer.cpu.D5700CPUMemoryAccess
import exception.InstructionException

internal class ConvertByteToAsciiInstruction(descriptor : Short, cpuMemory : D5700CPUMemoryAccess) : D5700Instruction(descriptor, cpuMemory)
{
	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		if ((descriptor.toInt() and 0xF) != 0)
			throw Exception("Invalid parameters")

		return arrayOf<Int>(
			(descriptor.toInt() shr 8 and 0xF),
			(descriptor.toInt() shr 4 and 0xF)
		)
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		var asciiVal : Int = cpuMemory.registers[parameters[0]].toInt()

		if (asciiVal <= 9)
			asciiVal += '0'.code
		else if (asciiVal <= 0xF)
			asciiVal += 'A'.code - 10
		else
			throw InstructionException("Convert to ASCII: Value at register ${parameters[0]} is outside of range 0-F.")

		cpuMemory.registers[parameters[1]] = asciiVal.toByte()
	}
}

