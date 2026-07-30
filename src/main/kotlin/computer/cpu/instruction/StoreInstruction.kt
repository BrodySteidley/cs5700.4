package computer.cpu.instruction

import computer.cpu.D5700CPUMemoryAccess

internal class StoreInstruction(descriptor : Short, cpuMemory : D5700CPUMemoryAccess) : D5700Instruction(descriptor, cpuMemory)
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

