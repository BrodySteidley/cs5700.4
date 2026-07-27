package component

import exception.InstructionException
import exception.MemoryOutOfBoundsException
import instruction.D5700Instruction
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException

class D5700CPU(rom : ROM, ram : D5700RAM, screen : D5700Screen, input : D5700Input)
{
	private val instructionExecutor = Executors.newSingleThreadScheduledExecutor()
	private val timerExecutor = Executors.newSingleThreadScheduledExecutor()
	private var instructionExecutorFuture : Future<*>? = null
	private var timerExecutorFuture : Future<*>? = null

	private val registers = D5700CPURegisters(rom, ram, screen, input)

	fun start()
	{
		instructionExecutorFuture = instructionExecutor.scheduleAtFixedRate(
		    ::performCurrentInstruction,
		    0,
		    1000L / 500L, // 500 hz
		    TimeUnit.MILLISECONDS
		)
		
		timerExecutorFuture = timerExecutor.scheduleAtFixedRate(
		    ::updateTimer,
		    0,
		    1000L / 60L, // 60hz
		    TimeUnit.MILLISECONDS
		)

		try {
			instructionExecutorFuture?.get()
		} catch (e: CancellationException) {
			instructionExecutor.shutdown()
		}
		catch (e: Exception)
		{
			System.err.println(e.message)
		}

		try {
			timerExecutorFuture?.get()
		} catch (e: CancellationException) {
			timerExecutor.shutdown()
		}
	}

	private fun halt()
	{
		instructionExecutorFuture?.cancel(true)
		timerExecutorFuture?.cancel(true)
	}
	
	private fun updateTimer()
	{
		synchronized(registers)
		{
			if (registers.timer > 0)
				registers.timer--
		}
	}

	private fun performCurrentInstruction()
	{
		synchronized(registers)
		{
			val addr: UShort = registers.programCounter.toUShort()
			val addr2: UShort = (registers.programCounter + 1).toUShort()

			var descriptor: Short

			try {
				descriptor = ((registers.ROMIO.read(addr).toInt() shl 8) or (registers.ROMIO.read(addr2)
					.toInt() and 0xFF)).toShort()
			} catch (e: MemoryOutOfBoundsException) {
				halt()
				return
			}

			try {
				D5700Instruction.perform(descriptor, registers)
			} catch (e: Exception) {
				throw e
			}
		}
	}
}

