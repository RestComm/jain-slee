package org.mobicents.slee.runtime.cache;

import java.util.Collection;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.legacy.write.CreateNodeCommand;
import org.jboss.cache.commands.read.ExistsCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.commands.read.GetKeyValueCommand;
import org.jboss.cache.commands.read.GetKeysCommand;
import org.jboss.cache.commands.read.GetNodeCommand;
import org.jboss.cache.commands.read.GravitateDataCommand;
import org.jboss.cache.commands.tx.CommitCommand;
import org.jboss.cache.commands.tx.OptimisticPrepareCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.commands.tx.RollbackCommand;
import org.jboss.cache.commands.write.ClearDataCommand;
import org.jboss.cache.commands.write.EvictCommand;
import org.jboss.cache.commands.write.InvalidateCommand;
import org.jboss.cache.commands.write.MoveCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.interceptors.base.CommandInterceptor;

/**
 * JBoss Cache interceptor to ensure the cache is used with a valid transaction
 * context.
 * 
 * @author martins
 * 
 */
public class MobicentsCommandInterceptor extends CommandInterceptor {

	private final TransactionManager txMgr;

	public MobicentsCommandInterceptor(TransactionManager txMgr) {
		super();
		this.txMgr = txMgr;
	}

	/**
	 * verifies that exists a transaction
	 * 
	 * @throws SystemException
	 */
	private void checkTransaction() throws SystemException {
		if (txMgr.getTransaction() == null) {
			throw new SystemException("no transaction");
		}
	}

	@Override
	public Object visitClearDataCommand(InvocationContext ctx,
			ClearDataCommand command) throws Throwable {
		checkTransaction();
		return super.visitClearDataCommand(ctx, command);
	}

	@Override
	public void visitCollection(InvocationContext arg0,
			Collection<? extends VisitableCommand> arg1) throws Throwable {
		checkTransaction();
		super.visitCollection(arg0, arg1);
	}

	@Override
	public Object visitCommitCommand(InvocationContext ctx,
			CommitCommand command) throws Throwable {
		checkTransaction();
		return super.visitCommitCommand(ctx, command);
	}

	@Override
	public Object visitCreateNodeCommand(InvocationContext ctx,
			CreateNodeCommand command) throws Throwable {
		checkTransaction();
		return super.visitCreateNodeCommand(ctx, command);
	}

	@Override
	public Object visitEvictFqnCommand(InvocationContext ctx,
			EvictCommand command) throws Throwable {
		checkTransaction();
		return super.visitEvictFqnCommand(ctx, command);
	}

	@Override
	public Object visitExistsNodeCommand(InvocationContext ctx,
			ExistsCommand command) throws Throwable {
		checkTransaction();
		return super.visitExistsNodeCommand(ctx, command);
	}

	@Override
	public Object visitGetChildrenNamesCommand(InvocationContext ctx,
			GetChildrenNamesCommand command) throws Throwable {
		checkTransaction();
		return super.visitGetChildrenNamesCommand(ctx, command);
	}

	@Override
	public Object visitGetDataMapCommand(InvocationContext ctx,
			GetDataMapCommand command) throws Throwable {
		checkTransaction();
		return super.visitGetDataMapCommand(ctx, command);
	}

	@Override
	public Object visitGetKeysCommand(InvocationContext ctx,
			GetKeysCommand command) throws Throwable {
		checkTransaction();
		return super.visitGetKeysCommand(ctx, command);
	}

	@Override
	public Object visitGetKeyValueCommand(InvocationContext ctx,
			GetKeyValueCommand command) throws Throwable {
		checkTransaction();
		return super.visitGetKeyValueCommand(ctx, command);
	}

	@Override
	public Object visitGetNodeCommand(InvocationContext ctx,
			GetNodeCommand command) throws Throwable {
		checkTransaction();
		return super.visitGetNodeCommand(ctx, command);
	}

	@Override
	public Object visitGravitateDataCommand(InvocationContext ctx,
			GravitateDataCommand command) throws Throwable {
		checkTransaction();
		return super.visitGravitateDataCommand(ctx, command);
	}

	@Override
	public Object visitInvalidateCommand(InvocationContext ctx,
			InvalidateCommand command) throws Throwable {
		checkTransaction();
		return super.visitInvalidateCommand(ctx, command);
	}

	@Override
	public Object visitMoveCommand(InvocationContext ctx, MoveCommand command)
			throws Throwable {
		checkTransaction();
		return super.visitMoveCommand(ctx, command);
	}

	@Override
	public Object visitOptimisticPrepareCommand(InvocationContext ctx,
			OptimisticPrepareCommand command) throws Throwable {
		checkTransaction();
		return super.visitOptimisticPrepareCommand(ctx, command);
	}

	@Override
	public Object visitPrepareCommand(InvocationContext ctx,
			PrepareCommand command) throws Throwable {
		checkTransaction();
		return super.visitPrepareCommand(ctx, command);
	}

	@Override
	public Object visitPutDataMapCommand(InvocationContext ctx,
			PutDataMapCommand command) throws Throwable {
		checkTransaction();
		return super.visitPutDataMapCommand(ctx, command);
	}

	@Override
	public Object visitPutForExternalReadCommand(InvocationContext ctx,
			PutForExternalReadCommand command) throws Throwable {
		checkTransaction();
		return super.visitPutForExternalReadCommand(ctx, command);
	}

	@Override
	public Object visitPutKeyValueCommand(InvocationContext ctx,
			PutKeyValueCommand command) throws Throwable {
		checkTransaction();
		return super.visitPutKeyValueCommand(ctx, command);
	}

	@Override
	public Object visitRemoveKeyCommand(InvocationContext ctx,
			RemoveKeyCommand command) throws Throwable {
		checkTransaction();
		return super.visitRemoveKeyCommand(ctx, command);
	}

	@Override
	public Object visitRemoveNodeCommand(InvocationContext ctx,
			RemoveNodeCommand command) throws Throwable {
		checkTransaction();
		return super.visitRemoveNodeCommand(ctx, command);
	}

	@Override
	public Object visitRollbackCommand(InvocationContext ctx,
			RollbackCommand command) throws Throwable {
		checkTransaction();
		return super.visitRollbackCommand(ctx, command);
	}
}
