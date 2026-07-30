package computer.cpu.instruction

import computer.cpu.D5700CPUMemoryAccess
import exception.InstructionParameterException

internal class ToggleMemoryInstruction(cpuMemory : D5700CPUMemoryAccess) : D5700Instruction(cpuMemory)
{
	override fun splitDescriptor(descriptor : Short) : Array<Int>
	{
		if ((descriptor.toInt() and 0xFFF) != 0)
			throw InstructionParameterException("TOGGLE_MEMORY")
		return arrayOf<Int>()
	}

	override fun performInstruction(parameters : Array<Int>)
	{
		cpuMemory.memory = !cpuMemory.memory
	}
}

