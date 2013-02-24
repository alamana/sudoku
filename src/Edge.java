public class Edge {

	public Node n1, n2;

	public Edge(Node a, Node b) {
		n1 = a;
		n2 = b;
	}

	public boolean equals(Object o) {
		boolean ret = false;
		Edge temp = (Edge) o;
		if (temp.n1.id == this.n1.id && temp.n2.id == this.n2.id)
			ret = true;
		return false;
	}
}
