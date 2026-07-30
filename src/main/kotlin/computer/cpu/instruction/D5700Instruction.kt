package computer.cpu.instruction

import computer.cpu.D5700CPUMemoryAccess

internal abstract class D5700Instruction(
	private val descriptor : Short,
	protected val cpuMemory : D5700CPUMemoryAccess
)
{
	fun perform()
	{	/* template */
		val parameters = splitDescriptor(descriptor)
		performInstruction(parameters)
		incrementProgramCounter()
	}

	protected open fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		return arrayOf<Int>(
			(descriptor.toInt() shr 8 and 0xF),
			(descriptor.toInt() shr 4 and 0xF),
			(descriptor.toInt() and 0xF)
		)
	}

	protected abstract fun performInstruction(parameters : Array<Int>)
	
	protected open fun incrementProgramCounter()
	{
		cpuMemory.programCounter = (cpuMemory.programCounter + 2).toShort()
	}
}

