package brainstorm.core

class MindMap (var name: String, val root: Node) {
  def this(name: String) = this(name, new Node(name, None))
}

object MindMap {
  def fromText(text: Iterator[String], name: String) = {
    if (text.hasNext) {
      new MindMap(name, Node.fromText(text, None))
    } else {
      new MindMap(name)
    }
  }
}
