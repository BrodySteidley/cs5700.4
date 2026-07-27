package instruction

import component.D5700CPURegisters
import exception.InstructionException

class JumpInstruction(cpuMemory : D5700CPURegisters) : D5700Instruction(cpuMemory)
{
	private var jumpLocation : Short = 0;

	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		return arrayOf<Int>(
			(descriptor.toInt() and 0xFFF),
		)
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		if (parameters[0] % 2 != 0)
			throw InstructionException("Invalid jump location for JUMP")

		jumpLocation = parameters[0].toShort()
	}

	override fun incrementProgramCounter()
	{
		cpuMemory.programCounter = jumpLocation;
	}
}

