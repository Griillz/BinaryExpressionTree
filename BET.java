package me.tony.databet;

import java.util.*;

public class BET {

	// private nested node class for constructing Binary Tree Nodes
	private class BinaryNode<E> {

		private BinaryNode parent;
		private BinaryNode left;
		private BinaryNode right;
		private E element;

		// Default constructor for a binary node
		public BinaryNode(BinaryNode<E> p, BinaryNode<E> r, BinaryNode<E> l, E e) {
			parent = p;
			left = l;
			right = r;
			element = e;
		}

		// method to check if a binary node is the root of the tree
		public boolean isRoot() {
			if (this.getParent() == null) {
				return true;
			}

			return false;
		}

		// method to check if a binary node is a leaf node
		public boolean isLeaf() {
			if (this.getLeft() == null && this.getRight() == null) {
				return true;
			}
			return false;
		}

		// method that gets the element of a node
		public E getElement() {
			return this.element;
		}

		// method that gets the parent of a node
		public BinaryNode<E> getParent() {
			return parent;
		}

		// method that gets the left child of a node
		public BinaryNode<E> getLeft() {
			return left;
		}

		// method that gets the right child of a node
		public BinaryNode<E> getRight() {
			return right;
		}

	}
	// end of node class

	// Member data
	private Stack<BinaryNode<String>> stack = new Stack<BinaryNode<String>>();
	private Stack<String> infixStack = new Stack<String>();
	private String[] expression;
	private BinaryNode<String> root = null;
	private int count = 0;
	private int leaves = 0;
	private Queue<String> infixQ = new LinkedList<>();
	private boolean oneOp = false;

	// default Binary Expression Tree constructor
	public BET() {

	}

	// Binary Expression Tree constructor that takes in a string, and a char that
	// determines infix or postfix
	public BET(String expr, char mode) {
		switch (mode) {

		case 'p':
			buildFromPostfix(expr);
			break;

		case 'i':
			buildFromInfix(expr);
			break;

		default:
			throw new IllegalStateException("Invalid Notation");
		}

	}

	// Builds the binary tree given a postfix notation
	public boolean buildFromPostfix(String postfix) {
		try {
			if (!isEmpty()) {
				makeEmpty(root);
			}

			delimiter(postfix);

			for (int i = 0; i < expression.length; i++) {

				if (isOperator(expression[i])) {
					oneOp = true;
					BinaryNode<String> node = new BinaryNode<String>(null, stack.pop(), stack.pop(), expression[i]);
					node.getLeft().parent = node;
					node.getRight().parent = node;
					stack.push(node);
					root = node;

				} else {
					BinaryNode<String> node = new BinaryNode<String>(null, null, null, expression[i]);
					stack.push(node);
				}
			}
			if(oneOp == false) {
				throw new IllegalStateException("Invalid Notation");
			}
		} catch (Exception e) {
			throw new IllegalStateException("Invalid Notation");
		}

		return true;
	}

	// builds the binary tree given an infix notation
	// Entire method converts the input from infix to postfix, then passes in the
	// converted postfix notation
	// into buildFromPostfix method at the end
	public boolean buildFromInfix(String infix) {
		try {
			if (!isEmpty()) {
				makeEmpty(root);
			}

			String[] expr = infix.split(" ");
			for (int i = 0; i < expr.length; i++) {

				if (isOperator(expr[i])) {
					oneOp = true;
					while (!infixStack.isEmpty() && greaterPrecedence(expr[i], infixStack.peek())
							&& !infixStack.peek().equals("(")) {
						infixQ.add(infixStack.pop());
					}

					infixStack.push(expr[i]);
				}

				else if (expr[i].equals("(")) {
					infixStack.push(expr[i]);
				}

				else if (expr[i].equals(")")) {
					while (!infixStack.peek().equals("(")) {
						infixQ.add(infixStack.pop());

					}
					if (infixStack.peek().equals("(")) {
						infixStack.pop();
					}
				} else {
					infixQ.add(expr[i]);
				}

			}
			
			if(oneOp == false) {
				throw new IllegalStateException("Invalid Notation");
			}

			while (!infixStack.isEmpty()) {
				if (infixStack.peek().equals("(")) {
					throw new IllegalStateException("Invalid Notation");
				}
				infixQ.add(infixStack.pop());
			}
			
			

		} catch (Exception e) {
			throw new IllegalStateException("Invalid Notation");

		}

		String postfix = "";
		String queue = infixQ.toString();

		for (int i = 0; i < queue.length(); i++) {
			if (queue.charAt(i) == '[' || queue.charAt(i) == ']' || queue.charAt(i) == ',') {
				postfix = postfix;

			} else {
				postfix = postfix + queue.charAt(i);
			}
		}
		buildFromPostfix(postfix);
		return true;
	}

	// prints a binary tree using an infix traversal, calls recursive method
	public void printInfixExpression() {
		printInfixExpression(root);
		System.out.println();
	}

	// prints a binary tree using a postfix traversal, calls recursive method
	public void printPostfixExpression() {
		printPostfixExpression(root);
		System.out.println();
	}

	// returns the size of a tree (size being number of nodes), calls recursive
	// method
	public int size() {
		count = 0;
		return size(root);
	}

	// returns if the tree is empty or not
	public boolean isEmpty() {
		return size() == 0;
	}

	// returns the number of leaf nodes in a tree, calls recursive method
	public int leafNodes() {
		leaves = 0;
		return leafNodes(root);
	}

	// Recursively prints a binary tree using an infix traversal
	private void printInfixExpression(BinaryNode<String> n) {
		if (n.getLeft() != null) {
			System.out.print("( ");
			printInfixExpression(n.getLeft());
		}

		System.out.print(n.getElement() + " ");

		if (n.getRight() != null) {
			printInfixExpression(n.getRight());
			System.out.print(") ");

		}

	}

	// Recursively makes a tree empty if it is not empty already
	private void makeEmpty(BinaryNode<String> t) {
		oneOp = false;
		if (t.getLeft() != null) {
			makeEmpty(t.getLeft());
		}
		if (t.getRight() != null) {
			makeEmpty(t.getRight());
		}
		t.left = null;
		t.right = null;
		t.parent = null;
		t.element = null;
	}

	// Recursively prints a tree using a postfix traversal
	private void printPostfixExpression(BinaryNode<String> n) {
		if (n.getLeft() != null) {
			printPostfixExpression(n.getLeft());
		}
		if (n.getRight() != null) {
			printPostfixExpression(n.getRight());
		}
		System.out.print(n.getElement() + " ");
	}

	// Recursively gets the size of a tree
	private int size(BinaryNode<String> t) {
		if (t == null) {
			return 0;
		}
		if (t.getLeft() != null) {
			size(t.getLeft());
		}
		if (t.getRight() != null) {
			size(t.getRight());
		}
		count++;
		return count;
	}

	// Recursively gets the amount of leaf nodes in a tree
	private int leafNodes(BinaryNode<String> t) {
		if (t == null) {
			return 0;
		}
		if (t.getLeft() != null) {
			leafNodes(t.getLeft());
		}
		if (t.getRight() != null) {
			leafNodes(t.getRight());
		}
		if (t.getLeft() == null && t.getRight() == null) {
			leaves++;
		}
		return leaves;
	}

	// Removes the spaces in an expression, making it easier to work with
	public void delimiter(String expr) {
		expression = expr.split(" ");
	}

	// Helper method that checks if a string is an operator
	public boolean isOperator(String op) {

		switch (op) {
		case "+":
			return true;
		case "-":
			return true;
		case "*":
			return true;
		case "/":
			return true;
		default:
			return false;
		}
	}

	// Helper method that checks if the current operator has greater precedence
	// than the operator on the top of the operator stack
	public boolean greaterPrecedence(String expr, String stackTop) {
		if (!infixStack.isEmpty()) {
			if (expr.equals("*") && stackTop.equals("+")) {
				return false;
			}
			if (expr.equals("/") && stackTop.equals("+")) {
				return false;
			}
			if (expr.equals("*") && stackTop.equals("-")) {
				return false;
			}
			if (expr.equals("/") && stackTop.equals("-")) {
				return false;
			}
		}
		return true;
	}

}
