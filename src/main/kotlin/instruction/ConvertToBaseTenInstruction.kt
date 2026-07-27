package instruction

import component.D5700CPURegisters
import exception.InstructionParameterException

class ConvertToBaseTenInstruction(cpuMemory : D5700CPURegisters) : D5700Instruction(cpuMemory)
{
	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		if ((descriptor.toInt() and 0xF) != 0)
			throw InstructionParameterException("CONVERT_TO_BASE_TEN")
		return arrayOf<Int>( (descriptor.toInt() shr 4 and 0xFF))
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		val v : Int = cpuMemory.registers[parameters[0]].toUByte().toInt()
		val hundreds = v / 100
		val tens = (v / 10) % 10
		val ones = v % 10

		cpuMemory.writeSelectedMemory(hundreds.toByte())
		cpuMemory.address++

		cpuMemory.writeSelectedMemory(tens.toByte())
		cpuMemory.address++

		cpuMemory.writeSelectedMemory(ones.toByte())

		cpuMemory.address = (cpuMemory.address - 2).toShort()
	}
}

