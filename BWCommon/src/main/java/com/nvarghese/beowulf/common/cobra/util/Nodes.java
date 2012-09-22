package com.nvarghese.beowulf.common.cobra.util;

import org.w3c.dom.Node;

public class Nodes {

	public static Node getCommonAncestor(final Node node1, final Node node2) {

		if (node1 == null || node2 == null) {
			return null;
		}
		Node checkNode = node1;
		while (!isSameOrAncestorOf(checkNode, node2)) {
			checkNode = checkNode.getParentNode();
			if (checkNode == null) {
				return null;
			}
		}
		return checkNode;
	}

	public static boolean isSameOrAncestorOf(final Node node, final Node child) {

		if (child.isSameNode(node)) {
			return true;
		}
		Node parent = child.getParentNode();
		if (parent == null) {
			return false;
		}
		return isSameOrAncestorOf(node, parent);
	}
}
