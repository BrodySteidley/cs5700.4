package computer.cpu.instruction

import computer.cpu.D5700CPUMemoryAccess

internal abstract class D5700Instruction(
	protected val cpuMemory : D5700CPUMemoryAccess
)
{
	companion object /* factory companion object */
	{
		private val instructionMap = mapOf<Int, (D5700CPUMemoryAccess) -> D5700Instruction> (
			0x0 to ::StoreInstruction,
			0x1 to ::AddInstruction,
			0x2 to ::SubInstruction,
			0x3 to ::ReadInstruction,
			0x4 to ::WriteInstruction,
			0x5 to ::JumpInstruction,
			0x6 to ::ReadKeyboardInstruction,
			0x7 to ::ToggleMemoryInstruction,
			0x8 to ::SkipEqualInstruction,
			0x9 to ::SkipNotEqualInstruction,
			0xA to ::SetAInstruction,
			0xB to ::SetTInstruction,
			0xC to ::ReadTInstruction,
			0xD to ::ConvertToBaseTenInstruction,
			0xE to ::ConvertByteToAsciiInstruction,
			0xF to ::DrawInstruction,
		)

		/* static factory method
		* I like D5700Instruction.perform(), rather than having it in a separate factory class */
		fun perform(descriptor : Short, cpuMemory : D5700CPUMemoryAccess)
		{
			val nybble = (descriptor.toInt() shr 12) and 0xF
			val concreteInstruction = instructionMap[nybble]!!.invoke(cpuMemory)

			val parameters = concreteInstruction.splitDescriptor(descriptor)
			concreteInstruction.performInstruction(parameters)
			concreteInstruction.incrementProgramCounter()
		}
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

