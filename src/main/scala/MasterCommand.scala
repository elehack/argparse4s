/* Copyright ⓒ 2012 Michael Ekstrand
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject
 * to the following conditions:
 *
 *  - The above copyright notice and this permission notice shall be
 *    included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.elehack.argparse4s

import net.sourceforge.argparse4j.inf.Subparsers

/**
 * Trait for commands with subcommands.
 */
trait MasterCommand
extends Command {
  /**
   * Get the subcommands for this command, if any. By default, there are
   * no subcommands.
   */
  def subcommands: Seq[Subcommand]

  /**
   * Get the subcommand, if one is provided.
   */
  def subcommand(implicit exc: ExecutionContext): Option[Subcommand] = {
    Option(exc.namespace.get[Subcommand](MasterCommand.subcommandDest))
  }

  /**
   * Configure the subparsers. The default implementation just adds the
   * subcommands; override this to e.g. set the help string.
   */
  protected def configureSubparsers(sub: Subparsers) {
    for (sc <- subcommands) {
      sc.addParser(sub)
    }
  }

  override def parser = {
    val parser = super.parser
    val sub = parser.addSubparsers
    configureSubparsers(sub)
    parser
  }
}

object MasterCommand {
  /**
   * The destination name for the subcommand to execute.
   */
  val subcommandDest = "argparse4j_subcommand"
}
