package org.loda.lalfred;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.loda.lalfred.util.Assert;

/**
 * 三项查找树
 * 
 * 比R项更加节省空间。由于本项目中需要存储英文单词和中文汉字，所以需要一张较大的字符表，采用三项查找树不会引发内存巨大消耗
 * 
 * @author minjun
 *
 */
public class TST<V> {

	class Node {
		char ch;
		V val;
		Node left;
		Node mid;
		Node right;

		@Override
		public String toString() {
			return "Node [ch=" + ch + ", val=" + val + "]";
		}

	}

	private Node root;

	private int size;

	public void put(String key, V value) {
		Assert.notNull(key);
		root = put(key, value, root, 0);
	}

	private Node put(String key, V value, Node node, int index) {
		if (node == null) {
			node = new Node();
			node.ch = key.charAt(index);
		}
		char c = key.charAt(index);
		if (c > node.ch) {
			node.right = put(key, value, node.right, index);
		} else if (c < node.ch) {
			node.left = put(key, value, node.left, index);
		} else if (!reachEnd(key, index)) {
			node.mid = put(key, value, node.mid, index + 1);
		} else {
			if (node.val == null) {
				size++;
			}
			node.val = value;
		}

		return node;
	}

	public V get(String key) {
		Assert.notNull(key);
		Node n = get(key, root, 0);
		return n == null ? null : n.val;
	}

	private Node get(String key, Node node, int index) {
		if (node == null) {
			return null;
		}
		if (key.isEmpty()) {
			return node;
		}

		char c = key.charAt(index);
		if (c > node.ch) {
			return get(key, node.right, index);
		} else if (c < node.ch) {
			return get(key, node.left, index);
		} else if (!reachEnd(key, index)) {
			return get(key, node.mid, index + 1);
		} else {
			return node;
		}
	}

	private boolean reachEnd(String key, int index) {
		return index >= key.length() - 1;
	}

	public void delete(String key) {
		Assert.notNull(key);
		delete(key, root, 0);
	}

	private void delete(String key, Node node, int index) {
		// TODO
	}

	public boolean contains(String key) {
		return get(key) != null;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return size;
	}

	public List<String> keys() {
		return keysWithPrefix("");
	}

	public List<String> keysThatMatch(String pattern) {
		List<String> list = new ArrayList<>();
		keysThatMatch(root, pattern, 0, list);
		return list;
	}

	private void keysThatMatch(Node node, String key, int index, List<String> list) {
		// if (node == null) {
		// return;
		// }
		//
		// char c = key.charAt(index);
		// if (c == '.') {
		// keysThatMatch(node, key, index + 1, list);
		// } else if (c > node.ch) {
		// keysThatMatch(node.right, key, index, list);
		// } else if (c < node.ch) {
		// keysThatMatch(node.left, key, index, list);
		// } else if (!reachEnd(key, index)) {
		// keysThatMatch(node.mid, key, index + 1, list);
		// } else if (node.val != null) {
		// list.add();
		// }
	}

	public List<String> keysWithPrefix(String prefix) {
		Node node = get(prefix, root, 0);
		if (node == null) {
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<>();
		if (node.val != null) {
			list.add(prefix);
		}
		if (!prefix.isEmpty()) {
			node = node.mid;
		}
		keysWithPrefix(node, prefix, list);
		return list;
	}

	private void keysWithPrefix(Node node, String prefix, List<String> list) {
		if (node == null) {
			return;
		}

		if (node.val != null) {
			list.add(prefix + node.ch);
		}

		keysWithPrefix(node.left, prefix, list);
		keysWithPrefix(node.right, prefix, list);
		keysWithPrefix(node.mid, prefix + node.ch, list);
	}

	public List<V> valuesWithPrefix(String prefix) {
		Node node = get(prefix, root, 0);
		if (node == null) {
			return Collections.emptyList();
		}
		List<V> list = new ArrayList<>();
		if (node.val != null) {
			list.add(node.val);
		}
		if (!prefix.isEmpty()) {
			node = node.mid;
		}
		valuesWithPrefix(node, prefix, list);
		return list;
	}

	private void valuesWithPrefix(Node node, String prefix, List<V> list) {
		if (node == null) {
			return;
		}

		if (node.val != null) {
			list.add(node.val);
		}

		valuesWithPrefix(node.left, prefix, list);
		valuesWithPrefix(node.right, prefix, list);
		valuesWithPrefix(node.mid, prefix + node.ch, list);
	}

	public String longestPrefixOf(String prefix) {
		String longest = null;
		for (String s : keysWithPrefix(prefix)) {
			if (len(s) > len(longest)) {
				longest = s;
			}
		}
		return longest;
	}

	private int len(String s) {
		return s == null ? 0 : s.length();
	}

}
