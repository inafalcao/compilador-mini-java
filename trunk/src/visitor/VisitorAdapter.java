package visitor;
import syntaxtree.*;

import visitor.Visitor;

public class VisitorAdapter implements Visitor
{
	
	public VisitorAdapter()
	{
		super();
	}

	@Override
	public void visit(Program node)
	{
	}

	@Override
	public void visit(MainClass node)
	{
	}

	@Override
	public void visit(ClassDeclSimple node)
	{
	}

	@Override
	public void visit(ClassDeclExtends node)
	{
	}

	@Override
	public void visit(VarDecl node)
	{
	}

	@Override
	public void visit(MethodDecl node)
	{
	}

	@Override
	public void visit(Formal node)
	{
	}

	@Override
	public void visit(IntArrayType node)
	{
	}

	@Override
	public void visit(BooleanType node)
	{
	}

	@Override
	public void visit(IntegerType node)
	{
	}

	@Override
	public void visit(IdentifierType node)
	{
	}

	@Override
	public void visit(Block node)
	{
	}

	@Override
	public void visit(If node)
	{
	}

	@Override
	public void visit(While node)
	{
	}

	@Override
	public void visit(Print node)
	{
	}

	@Override
	public void visit(Assign node)
	{
	}

	@Override
	public void visit(ArrayAssign node)
	{
	}

	@Override
	public void visit(And node)
	{
	}

	@Override
	public void visit(LessThan node)
	{
	}

	@Override
	public void visit(Plus node)
	{
	}

	@Override
	public void visit(Minus node)
	{
	}

	@Override
	public void visit(Times node)
	{
	}

	@Override
	public void visit(ArrayLookup node)
	{
	}

	@Override
	public void visit(ArrayLength node)
	{
	}

	@Override
	public void visit(Call node)
	{
	}

	@Override
	public void visit(IntegerLiteral node)
	{
	}

	@Override
	public void visit(True node)
	{
	}

	@Override
	public void visit(False node)
	{
	}

	@Override
	public void visit(This node)
	{
	}

	@Override
	public void visit(NewArray node)
	{
	}

	@Override
	public void visit(NewObject node)
	{
	}

	@Override
	public void visit(Not node)
	{
	}

	@Override
	public void visit(IdentifierExp node)
	{
	}

	@Override
	public void visit(Identifier node)
	{
	}
	
	@Override
	public void visit(StringArrayType node)
	{
	}
	@Override
	public void visit(NullType node)
	{
	}
}
