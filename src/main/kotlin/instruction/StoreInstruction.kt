package instruction

import component.D5700CPURegisters

class StoreInstruction(cpuMemory : D5700CPURegisters) : D5700Instruction(cpuMemory)
{
	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		return arrayOf<Int>(
			(descriptor.toInt() shr 8 and 0xF),
			(descriptor.toInt() and 0xFF)
		)
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		cpuMemory.registers[parameters[0]] = parameters[1].toByte()
	}
}

