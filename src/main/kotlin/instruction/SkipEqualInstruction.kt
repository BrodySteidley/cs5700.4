package instruction

import component.D5700CPURegisters
import exception.InstructionParameterException

open class SkipEqualInstruction(cpuMemory : D5700CPURegisters) : D5700Instruction(cpuMemory)
{
	protected var skip : Boolean = false;
	
	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		if ((descriptor.toInt() and 0xF) != 0)
			throw InstructionParameterException("SKIP_EQUAL")

		return arrayOf<Int>(
			(descriptor.toInt() shr 8 and 0xF),
			(descriptor.toInt() shr 4 and 0xF)
		)
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		skip = (cpuMemory.registers[parameters[0]] == cpuMemory.registers[parameters[1]])
	}

	override fun incrementProgramCounter()
	{
		cpuMemory.programCounter = (cpuMemory.programCounter + if (skip) 4 else 2).toShort()
	}
}

