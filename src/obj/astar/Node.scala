package obj.astar

import obj.Pos


class Node(var parent: Node = null, val current: Pos, var action: () => Unit = null) {}
