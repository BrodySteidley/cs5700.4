package computer.cpu

import computer.D5700Input
import computer.D5700RAM
import computer.D5700Screen
import computer.ROM
import exception.MemoryOutOfBoundsException
import computer.cpu.instruction.D5700Instruction
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

	private val memory = D5700CPUMemoryAccess(rom, ram, screen, input)

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
		synchronized(memory)
		{
			if (memory.timer > 0)
				memory.timer--
		}
	}

	private fun performCurrentInstruction()
	{
		synchronized(memory)
		{
			val addr: UShort = memory.programCounter.toUShort()
			val addr2: UShort = (memory.programCounter + 1).toUShort()

			var descriptor: Short

			try {
				descriptor = ((memory.ROMIO.read(addr).toInt() shl 8) or (memory.ROMIO.read(addr2)
					.toInt() and 0xFF)).toShort()
			} catch (e: MemoryOutOfBoundsException) {
				halt()
				return
			}

			try {
				D5700Instruction.perform(descriptor, memory)
			} catch (e: Exception) {
				throw e
			}
		}
	}
}

