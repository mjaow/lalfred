package org.loda.lalfred;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.loda.lalfred.util.Assert;

/**
 * 三项查找树
 * 
 * 比R项更加节省空间。由于本项目中需要存储英文单词和中文汉字，所以需要一张较大的字符表，采用三项查找树不会引发内存巨大消耗
 * 
 * 改进：前缀树+倒排索引结构
 * 
 * 当用户输入某个单词的首字母时，前缀树可以搜到对应的单词，利用倒排索引可以根据这个单词查到他所属的文件
 * 
 * @author minjun
 *
 */
public class TST<V> {

	class Node {
		char ch;
		Set<V> val;
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

	private <E> Set<E> newSet() {
		return new HashSet<>();
	}

	private String lowerCase(String s) {
		return StringUtils.lowerCase(s);
	}

	public void put(String key, V value) {
		Assert.notNull(key);
		root = put(lowerCase(key), value, root, 0);
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
				node.val = newSet();
			}
			node.val.add(value);
		}

		return node;
	}

	public Set<V> get(String key) {
		Assert.notNull(key);
		Node n = get(lowerCase(key), root, 0);
		return n == null ? Collections.emptySet() : n.val;
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

	public void delete(String key, V value) {
		Assert.notNull(key);
		root = delete(lowerCase(key), value, root, 0);
	}

	private Node delete(String key, V value, Node node, int index) {
		if (node == null) {
			return null;
		}

		if (key.isEmpty()) {
			return null;
		}

		char c = key.charAt(index);
		if (c > node.ch) {
			node.right = delete(key, value, node.right, index);
		} else if (c < node.ch) {
			node.left = delete(key, value, node.left, index);
		} else if (!reachEnd(key, index)) {
			node.mid = delete(key, value, node.mid, index + 1);
		} else if (node.val != null) {
			node.val.remove(value);
			if (node.val.isEmpty()) {
				node.val = null;
			}
		}

		if (node.left == null && node.right == null && (node.val == null || node.val.isEmpty())) {
			node = null;
		}
		return node;
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

	public Set<String> keys() {
		return keysWithPrefix("");
	}

	public Set<String> keysThatMatch(String pattern) {
		Set<String> set = newSet();
		keysThatMatch(root, lowerCase(pattern), 0, set);
		return set;
	}

	private void keysThatMatch(Node node, String key, int index, Set<String> set) {
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

	public Set<String> keysWithPrefix(String prefix) {
		prefix = lowerCase(prefix);
		Node node = get(prefix, root, 0);
		if (node == null) {
			return Collections.emptySet();
		}
		Set<String> set = newSet();
		if (node.val != null) {
			set.add(prefix);
		}
		if (!prefix.isEmpty()) {
			node = node.mid;
		}
		keysWithPrefix(node, prefix, set);
		return set;
	}

	private void keysWithPrefix(Node node, String prefix, Set<String> set) {
		if (node == null) {
			return;
		}

		if (node.val != null) {
			set.add(prefix + node.ch);
		}

		keysWithPrefix(node.left, prefix, set);
		keysWithPrefix(node.right, prefix, set);
		keysWithPrefix(node.mid, prefix + node.ch, set);
	}

	public Set<V> valuesWithPrefix(String prefix) {
		prefix = lowerCase(prefix);
		Node node = get(prefix, root, 0);
		if (node == null) {
			return Collections.emptySet();
		}
		Set<V> set = newSet();
		if (node.val != null) {
			set.addAll(node.val);
		}
		if (!prefix.isEmpty()) {
			node = node.mid;
		}
		valuesWithPrefix(node, prefix, set);
		return set;
	}

	private void valuesWithPrefix(Node node, String prefix, Set<V> set) {
		if (node == null) {
			return;
		}

		if (node.val != null) {
			set.addAll(node.val);
		}

		valuesWithPrefix(node.left, prefix, set);
		valuesWithPrefix(node.right, prefix, set);
		valuesWithPrefix(node.mid, prefix + node.ch, set);
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
