package computer.cpu.instruction

import computer.cpu.D5700CPURegisterAccess

internal class SetAInstruction(cpuMemory : D5700CPURegisterAccess) : D5700Instruction(cpuMemory)
{
	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		return arrayOf<Int>(
			(descriptor.toInt() and 0xFFF)
		)
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		cpuMemory.address = parameters[0].toShort()
	}
}

