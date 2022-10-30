package shortestpath;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.util.Scanner;

/**
 *
 * @author jly09
 */
public class ShortestPath {

    Dijkstra dj;
    Graph g;
    boolean isRunning;
    boolean init;

    String regEx;
    String initRegex;

    String input;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ShortestPath();
    }

    ShortestPath() {
        dj = new Dijkstra();
        g = new Graph();
        Scanner s = new Scanner(System.in);

        isRunning = true;
        init = true;
        regEx = "[A][ ][\\d]+[ ][\\d]+[ ][\\d]+|[Q|D][ ][\\d]+[ ][\\d]+";
        initRegex = "[\\d]+[ ][\\d]+[ ][\\d]+|[S]";

        // initialise graph
        System.out.println("Initialise graph:");
        while (init) {
            input = s.nextLine();
            if (checkInitialise(input) == false) {
                System.out.println("R");
                init = false;
            }
        }
        /*
        for (Node n : g.getNodeList()) {
            System.out.println("Start Node: " + n.getLabel());
            List<Edge> el = n.getEdgeList();
            for (Edge e : el) {
                System.out.println("End Node: " + e.getEndNode().getLabel());
                System.out.println("Weight: " + e.getWeight());
            }
        }
         */

        //addSmallGraph1();

        /*
        for (int i = 0; i < g.getEdgeList().size(); i++) {
            System.out.println(g.getEdgeList().get(i).getStartNode().getLabel() + " "
                    + g.getEdgeList().get(i).getEndNode().getLabel() + " "
                    + g.getEdgeList().get(i).getWeight());
        }
        */
        
        // accept commands
        System.out.println("Enter Command:");
        while (isRunning) {
            input = s.nextLine();
            if (checkCommand(input)) {
                String[] split = input.split("\\s+");

                // Execute Add Command
                if (split[0].charAt(0) == 'A') {
                    System.out.println("Add");
                    addEdge(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));

                    for (Node n : g.getNodeList()) {
                        System.out.println("Start Node: " + n.getLabel());
                        List<Edge> el = n.getEdgeList();
                        for (Edge e : el) {
                            System.out.println("End Node: " + e.getEndNode().getLabel());
                            System.out.println("Weight: " + e.getWeight());
                        }
                    }
                }

                // Execute Delete Command
                if (split[0].charAt(0) == 'D') {
                    System.out.println("Delete");
                    deleteEdge(Integer.valueOf(split[1]), Integer.valueOf(split[2]));
                    for (Node n : g.getNodeList()) {
                        System.out.println("Start Node: " + n.getLabel());
                        List<Edge> el = n.getEdgeList();
                        for (Edge e : el) {
                            System.out.println("End Node: " + e.getEndNode().getLabel());
                            System.out.println("Weight: " + e.getWeight());
                        }
                    }
                }

                // Execute Query
                if (split[0].charAt(0) == 'Q') {
                    System.out.println("Query");
                    calculateQuery(Integer.valueOf(split[1]), Integer.valueOf(split[2]));
                    
                }

            } else {
                System.out.println("Incorrect Command.");
            }
        }
        s.close();
    }

    // check if command matches format
    boolean checkCommand(String c) {
        if (c.matches(regEx)) {
            return true;
        }
        return false;
    }

    // check if inputs for initialising graph matches format
    boolean checkInitialise(String g) {
        if (g.matches(initRegex)) {
            String[] split = g.split("\\s+");

            // end initialise if S input
            if (g.charAt(0) == 'S') {
                return false;
            }

            // otherwise add edge to node
            addEdge(Integer.valueOf(split[0]),
                    Integer.valueOf(split[1]),
                    Integer.valueOf(split[2]));
        }
        else {
            System.out.println("Incorrect Input");
        }
        return true;
    }

    // add edge to a node
    public void addEdge(int sn, int en, int w) {
        
        // first check nodes exists
        Node startNode = checkNodeExists(sn);
        Node endNode = checkNodeExists(en);

        // if nodes does not exist create it and add it to nodes list
        if (startNode == null) {
            startNode = addNode(sn);
        }

        if (endNode == null) {
            endNode = addNode(en);
        }

        // add edge now that nodes exist
        startNode.addEdge(endNode, w);
        g.addEdge(startNode, endNode, w);
    }

    // add node to node list
    public Node addNode(int l) {
        Node newNode = new Node(l);
        g.getNodeList().add(newNode);
        return newNode;
    }

    // check if node exist in the node list
    public Node checkNodeExists(Integer l) {
        for (int i = 0; i < g.getNodeList().size(); i++) {
            if (g.getNodeList().get(i).getLabel() == l) {
                return g.getNodeList().get(i);
            }
        }

        return null;
    }

    // delete edge
    // if it exists delete it
    // if end node after delete does not have any edges going to or from it delete it
    public void deleteEdge(int sn, int en) {
        Node tempNode = checkNodeExists(sn);
        boolean flagForRemoval = false;
        int endNodePointer = -1;

        if (tempNode != null) {
            int pointer = tempNode.findPath(en);
            if (pointer > -1) {
                tempNode.getEdgeList().remove(pointer);

                g.deleteEdge(sn, en);

                for (int i = 0; i < g.getNodeList().size(); i++) {
                    if (g.getNodeList().get(i).getLabel() == en) {
                        endNodePointer = i;
                        if (g.getNodeList().get(i).getEdgeList().isEmpty()) {
                            flagForRemoval = true;
                        }
                    }

                    if (g.getNodeList().get(i).findPath(en) > -1) {
                        flagForRemoval = false;
                    }
                }
            }
        }
        if (flagForRemoval && endNodePointer > -1) {
            g.getNodeList().remove(endNodePointer);
        }
    }
    
    public void calculateQuery(int sn, int en) {
        Node startNode = checkNodeExists(sn);
        Node endNode = checkNodeExists(en);
        
        
        
        int pathValue = dj.calculatePath(g, sn, en);
        System.out.println("Output: " + pathValue);
    }

    public void addLargeGraph() {
        addEdge(1, 147, 5);
        addEdge(2, 218, 4);
        addEdge(3, 316, 4);
        addEdge(4, 16, 6);
        addEdge(5, 273, 1);
        addEdge(6, 157, 1);
        addEdge(7, 466, 2);
        addEdge(8, 148, 2);
        addEdge(9, 113, 4);
        addEdge(10, 80, 2);
        addEdge(11, 45, 3);
        addEdge(12, 328, 1);
        addEdge(13, 413, 6);
        addEdge(14, 322, 3);
        addEdge(15, 436, 3);
        addEdge(16, 450, 4);
        addEdge(17, 275, 6);
        addEdge(18, 2, 1);
        addEdge(19, 474, 1);
        addEdge(20, 25, 6);
        addEdge(21, 396, 3);
        addEdge(22, 498, 5);
        addEdge(23, 484, 5);
        addEdge(24, 479, 4);
        addEdge(25, 34, 3);
        addEdge(26, 162, 2);
        addEdge(27, 461, 2);
        addEdge(28, 156, 3);
        addEdge(29, 293, 4);
        addEdge(30, 427, 6);
        addEdge(31, 397, 4);
        addEdge(32, 107, 4);
        addEdge(33, 204, 1);
        addEdge(34, 445, 5);
        addEdge(35, 317, 5);
        addEdge(36, 342, 1);
        addEdge(37, 295, 1);
        addEdge(38, 417, 1);
        addEdge(39, 72, 5);
        addEdge(40, 311, 2);
        addEdge(41, 70, 1);
        addEdge(42, 297, 4);
        addEdge(43, 280, 4);
        addEdge(44, 338, 1);
        addEdge(45, 496, 2);
        addEdge(46, 253, 6);
        addEdge(47, 443, 3);
        addEdge(48, 453, 1);
        addEdge(49, 369, 5);
        addEdge(50, 435, 1);
        addEdge(51, 79, 5);
        addEdge(52, 415, 1);
        addEdge(53, 144, 3);
        addEdge(54, 102, 2);
        addEdge(55, 13, 4);
        addEdge(56, 9, 2);
        addEdge(57, 138, 2);
        addEdge(58, 289, 3);
        addEdge(59, 250, 5);
        addEdge(60, 305, 6);
        addEdge(61, 470, 2);
        addEdge(62, 446, 5);
        addEdge(63, 236, 2);
        addEdge(64, 238, 2);
        addEdge(65, 485, 6);
        addEdge(66, 264, 2);
        addEdge(67, 372, 4);
        addEdge(68, 12, 6);
        addEdge(69, 11, 6);
        addEdge(70, 350, 2);
        addEdge(71, 215, 4);
        addEdge(72, 272, 1);
        addEdge(73, 351, 6);
        addEdge(74, 164, 4);
        addEdge(75, 135, 2);
        addEdge(76, 58, 1);
        addEdge(77, 239, 1);
        addEdge(78, 271, 6);
        addEdge(79, 425, 4);
        addEdge(80, 26, 3);
        addEdge(81, 456, 5);
        addEdge(82, 75, 4);
        addEdge(83, 243, 1);
        addEdge(84, 346, 3);
        addEdge(85, 167, 2);
        addEdge(86, 333, 1);
        addEdge(87, 331, 3);
        addEdge(88, 48, 1);
        addEdge(89, 495, 6);
        addEdge(90, 170, 6);
        addEdge(91, 205, 6);
        addEdge(92, 132, 4);
        addEdge(93, 307, 4);
        addEdge(94, 476, 1);
        addEdge(95, 303, 4);
        addEdge(96, 290, 6);
        addEdge(97, 203, 2);
        addEdge(98, 337, 5);
        addEdge(99, 406, 6);
        addEdge(100, 188, 1);
        addEdge(101, 384, 4);
        addEdge(102, 371, 4);
        addEdge(103, 230, 2);
        addEdge(104, 28, 3);
        addEdge(105, 420, 1);
        addEdge(106, 497, 6);
        addEdge(107, 357, 1);
        addEdge(108, 6, 4);
        addEdge(109, 55, 5);
        addEdge(110, 288, 5);
        addEdge(111, 326, 4);
        addEdge(112, 349, 6);
        addEdge(113, 137, 3);
        addEdge(114, 21, 2);
        addEdge(115, 363, 3);
        addEdge(116, 7, 1);
        addEdge(117, 176, 6);
        addEdge(118, 368, 3);
        addEdge(119, 211, 6);
        addEdge(120, 409, 1);
        addEdge(121, 298, 3);
        addEdge(122, 18, 1);
        addEdge(123, 206, 2);
        addEdge(124, 86, 1);
        addEdge(125, 473, 3);
        addEdge(126, 219, 1);
        addEdge(127, 161, 3);
        addEdge(128, 419, 3);
        addEdge(129, 115, 4);
        addEdge(130, 404, 1);
        addEdge(131, 158, 2);
        addEdge(132, 224, 5);
        addEdge(133, 163, 1);
        addEdge(134, 306, 6);
        addEdge(135, 257, 4);
        addEdge(136, 47, 1);
        addEdge(137, 216, 1);
        addEdge(138, 201, 2);
        addEdge(139, 360, 2);
        addEdge(140, 364, 4);
        addEdge(141, 424, 2);
        addEdge(142, 116, 6);
        addEdge(143, 491, 6);
        addEdge(144, 418, 1);
        addEdge(145, 392, 6);
        addEdge(146, 296, 6);
        addEdge(147, 62, 4);
        addEdge(148, 398, 6);
        addEdge(149, 229, 5);
        addEdge(150, 449, 6);
        addEdge(151, 174, 6);
        addEdge(152, 159, 4);
        addEdge(153, 459, 1);
        addEdge(154, 17, 6);
        addEdge(155, 344, 2);
        addEdge(156, 33, 2);
        addEdge(157, 486, 6);
        addEdge(158, 64, 3);
        addEdge(159, 111, 2);
        addEdge(160, 493, 5);
        addEdge(161, 266, 1);
        addEdge(162, 430, 1);
        addEdge(163, 189, 6);
        addEdge(164, 421, 3);
        addEdge(165, 226, 3);
        addEdge(166, 190, 3);
        addEdge(167, 92, 3);
        addEdge(168, 213, 6);
        addEdge(169, 73, 2);
        addEdge(170, 153, 1);
        addEdge(171, 452, 5);
        addEdge(172, 434, 4);
        addEdge(173, 44, 1);
        addEdge(174, 302, 2);
        addEdge(175, 57, 5);
        addEdge(176, 441, 5);
        addEdge(177, 89, 4);
        addEdge(178, 94, 2);
        addEdge(179, 141, 4);
        addEdge(180, 91, 4);
        addEdge(181, 378, 2);
        addEdge(182, 389, 2);
        addEdge(183, 414, 3);
        addEdge(184, 320, 1);
        addEdge(185, 31, 6);
        addEdge(186, 60, 5);
        addEdge(187, 193, 3);
        addEdge(188, 106, 3);
        addEdge(189, 97, 3);
        addEdge(190, 354, 2);
        addEdge(191, 462, 4);
        addEdge(192, 487, 2);
        addEdge(193, 265, 6);
        addEdge(194, 251, 4);
        addEdge(195, 286, 3);
        addEdge(196, 128, 2);
        addEdge(197, 19, 2);
        addEdge(198, 140, 6);
        addEdge(199, 54, 1);
        addEdge(200, 114, 1);
        addEdge(201, 82, 3);
        addEdge(202, 258, 5);
        addEdge(203, 478, 3);
        addEdge(204, 268, 1);
        addEdge(205, 437, 5);
        addEdge(206, 256, 6);
        addEdge(207, 173, 4);
        addEdge(208, 467, 6);
        addEdge(209, 475, 1);
        addEdge(210, 10, 6);
        addEdge(211, 166, 6);
        addEdge(212, 63, 5);
        addEdge(213, 155, 3);
        addEdge(214, 198, 5);
        addEdge(215, 53, 1);
        addEdge(216, 222, 5);
        addEdge(217, 112, 1);
        addEdge(218, 197, 3);
        addEdge(219, 183, 4);
        addEdge(220, 77, 6);
        addEdge(221, 139, 5);
        addEdge(222, 194, 2);
        addEdge(223, 390, 2);
        addEdge(224, 123, 1);
        addEdge(225, 81, 3);
        addEdge(226, 471, 1);
        addEdge(227, 182, 6);
        addEdge(228, 220, 3);
        addEdge(229, 24, 1);
        addEdge(230, 5, 6);
        addEdge(231, 262, 5);
        addEdge(232, 448, 5);
        addEdge(233, 210, 3);
        addEdge(234, 481, 3);
        addEdge(235, 340, 6);
        addEdge(236, 192, 3);
        addEdge(237, 327, 2);
        addEdge(238, 1, 1);
        addEdge(239, 356, 5);
        addEdge(240, 500, 1);
        addEdge(241, 348, 5);
        addEdge(242, 101, 5);
        addEdge(243, 233, 4);
        addEdge(244, 125, 3);
        addEdge(245, 457, 1);
        addEdge(246, 100, 1);
        addEdge(247, 103, 3);
        addEdge(248, 353, 6);
        addEdge(249, 442, 2);
        addEdge(250, 68, 3);
        addEdge(251, 477, 2);
        addEdge(252, 379, 3);
        addEdge(253, 465, 5);
        addEdge(254, 246, 4);
        addEdge(255, 304, 1);
        addEdge(256, 41, 5);
        addEdge(257, 315, 4);
        addEdge(258, 429, 6);
        addEdge(259, 341, 4);
        addEdge(260, 225, 2);
        addEdge(261, 376, 6);
        addEdge(262, 187, 1);
        addEdge(263, 492, 3);
        addEdge(264, 160, 6);
        addEdge(265, 98, 1);
        addEdge(266, 300, 4);
        addEdge(267, 175, 1);
        addEdge(268, 228, 2);
        addEdge(269, 242, 5);
        addEdge(270, 282, 6);
        addEdge(271, 136, 3);
        addEdge(272, 78, 1);
        addEdge(273, 61, 6);
        addEdge(274, 276, 2);
        addEdge(275, 490, 6);
        addEdge(276, 120, 4);
        addEdge(277, 105, 3);
        addEdge(278, 426, 4);
        addEdge(279, 480, 3);
        addEdge(280, 87, 2);
        addEdge(281, 294, 5);
        addEdge(282, 422, 2);
        addEdge(283, 20, 4);
        addEdge(284, 145, 1);
        addEdge(285, 15, 3);
        addEdge(286, 40, 5);
        addEdge(287, 291, 2);
        addEdge(288, 260, 1);
        addEdge(289, 281, 6);
        addEdge(290, 152, 2);
        addEdge(291, 179, 3);
        addEdge(292, 274, 5);
        addEdge(293, 460, 2);
        addEdge(294, 117, 3);
        addEdge(295, 410, 2);
        addEdge(296, 329, 1);
        addEdge(297, 27, 5);
        addEdge(298, 108, 6);
        addEdge(299, 365, 4);
        addEdge(300, 332, 2);
        addEdge(301, 51, 1);
        addEdge(302, 74, 2);
        addEdge(303, 370, 2);
        addEdge(304, 154, 2);
        addEdge(305, 221, 6);
        addEdge(306, 382, 6);
        addEdge(307, 355, 5);
        addEdge(308, 172, 4);
        addEdge(309, 23, 6);
        addEdge(310, 52, 1);
        addEdge(311, 283, 5);
        addEdge(312, 325, 1);
        addEdge(313, 380, 6);
        addEdge(314, 444, 6);
        addEdge(315, 411, 1);
        addEdge(316, 488, 3);
        addEdge(317, 394, 3);
        addEdge(318, 440, 3);
        addEdge(319, 255, 1);
        addEdge(320, 279, 4);
        addEdge(321, 324, 1);
        addEdge(322, 267, 3);
        addEdge(323, 165, 6);
        addEdge(324, 212, 5);
        addEdge(325, 142, 4);
        addEdge(326, 69, 6);
        addEdge(327, 347, 3);
        addEdge(328, 237, 5);
        addEdge(329, 67, 2);
        addEdge(330, 385, 1);
        addEdge(331, 109, 5);
        addEdge(332, 177, 2);
        addEdge(333, 312, 2);
        addEdge(334, 463, 6);
        addEdge(335, 42, 6);
        addEdge(336, 314, 3);
        addEdge(337, 36, 5);
        addEdge(338, 217, 6);
        addEdge(339, 127, 4);
        addEdge(340, 438, 3);
        addEdge(341, 313, 6);
        addEdge(342, 469, 5);
        addEdge(343, 196, 2);
        addEdge(344, 83, 3);
        addEdge(345, 66, 5);
        addEdge(346, 231, 3);
        addEdge(347, 248, 1);
        addEdge(348, 56, 3);
        addEdge(349, 180, 6);
        addEdge(350, 284, 2);
        addEdge(351, 358, 4);
        addEdge(352, 143, 5);
        addEdge(353, 287, 6);
        addEdge(354, 199, 4);
        addEdge(355, 49, 1);
        addEdge(356, 339, 6);
        addEdge(357, 85, 4);
        addEdge(358, 362, 1);
        addEdge(359, 30, 5);
        addEdge(360, 200, 1);
        addEdge(361, 84, 6);
        addEdge(362, 59, 2);
        addEdge(363, 482, 3);
        addEdge(364, 99, 4);
        addEdge(365, 151, 1);
        addEdge(366, 209, 2);
        addEdge(367, 374, 4);
        addEdge(368, 412, 5);
        addEdge(369, 241, 2);
        addEdge(370, 319, 2);
        addEdge(371, 110, 4);
        addEdge(372, 249, 6);
        addEdge(373, 184, 3);
        addEdge(374, 330, 5);
        addEdge(375, 146, 2);
        addEdge(376, 32, 5);
        addEdge(377, 423, 2);
        addEdge(378, 124, 5);
        addEdge(379, 71, 1);
        addEdge(380, 126, 2);
        addEdge(381, 130, 5);
        addEdge(382, 395, 1);
        addEdge(383, 391, 2);
        addEdge(384, 401, 5);
        addEdge(385, 191, 2);
        addEdge(386, 150, 5);
        addEdge(387, 247, 6);
        addEdge(388, 208, 1);
        addEdge(389, 149, 2);
        addEdge(390, 458, 4);
        addEdge(391, 232, 4);
        addEdge(392, 76, 6);
        addEdge(393, 121, 5);
        addEdge(394, 407, 1);
        addEdge(395, 489, 2);
        addEdge(396, 455, 2);
        addEdge(397, 96, 4);
        addEdge(398, 499, 5);
        addEdge(399, 185, 2);
        addEdge(400, 464, 6);
        addEdge(401, 252, 1);
        addEdge(402, 377, 6);
        addEdge(403, 359, 2);
        addEdge(404, 277, 4);
        addEdge(405, 399, 1);
        addEdge(406, 388, 5);
        addEdge(407, 181, 2);
        addEdge(408, 323, 6);
        addEdge(409, 22, 3);
        addEdge(410, 270, 2);
        addEdge(411, 483, 4);
        addEdge(412, 134, 3);
        addEdge(413, 171, 2);
        addEdge(414, 39, 3);
        addEdge(415, 433, 4);
        addEdge(416, 299, 6);
        addEdge(417, 494, 1);
        addEdge(418, 321, 5);
        addEdge(419, 65, 3);
        addEdge(420, 310, 6);
        addEdge(421, 178, 3);
        addEdge(422, 133, 3);
        addEdge(423, 46, 1);
        addEdge(424, 90, 3);
        addEdge(425, 454, 6);
        addEdge(426, 29, 2);
        addEdge(427, 37, 1);
        addEdge(428, 334, 6);
        addEdge(429, 309, 1);
        addEdge(430, 381, 2);
        addEdge(431, 43, 5);
        addEdge(432, 88, 4);
        addEdge(433, 472, 4);
        addEdge(434, 292, 3);
        addEdge(435, 383, 6);
        addEdge(436, 335, 5);
        addEdge(437, 408, 1);
        addEdge(438, 240, 3);
        addEdge(439, 352, 2);
        addEdge(440, 169, 4);
        addEdge(441, 400, 5);
        addEdge(442, 447, 3);
        addEdge(443, 366, 2);
        addEdge(444, 343, 4);
        addEdge(445, 439, 1);
        addEdge(446, 129, 1);
        addEdge(447, 93, 1);
        addEdge(448, 118, 4);
        addEdge(449, 263, 5);
        addEdge(450, 278, 1);
        addEdge(451, 386, 4);
        addEdge(452, 104, 4);
        addEdge(453, 361, 3);
        addEdge(454, 301, 2);
        addEdge(455, 285, 5);
        addEdge(456, 405, 2);
        addEdge(457, 50, 1);
        addEdge(458, 468, 4);
        addEdge(459, 168, 3);
        addEdge(460, 35, 2);
        addEdge(461, 195, 5);
        addEdge(462, 122, 3);
        addEdge(463, 254, 3);
        addEdge(464, 14, 1);
        addEdge(465, 234, 5);
        addEdge(466, 373, 5);
        addEdge(467, 375, 5);
        addEdge(468, 3, 6);
        addEdge(469, 308, 4);
        addEdge(470, 207, 5);
        addEdge(471, 244, 2);
        addEdge(472, 235, 1);
        addEdge(473, 259, 5);
        addEdge(474, 214, 5);
        addEdge(475, 119, 4);
        addEdge(476, 38, 5);
        addEdge(477, 403, 3);
        addEdge(478, 318, 4);
        addEdge(479, 432, 4);
        addEdge(480, 345, 6);
        addEdge(481, 186, 4);
        addEdge(482, 95, 6);
        addEdge(483, 269, 6);
        addEdge(484, 245, 6);
        addEdge(485, 416, 3);
        addEdge(486, 336, 3);
        addEdge(487, 202, 2);
        addEdge(488, 223, 4);
        addEdge(489, 431, 3);
        addEdge(490, 8, 1);
        addEdge(491, 367, 1);
        addEdge(492, 131, 1);
        addEdge(493, 451, 4);
        addEdge(494, 393, 3);
        addEdge(495, 261, 3);
        addEdge(496, 428, 4);
        addEdge(497, 4, 1);
        addEdge(498, 387, 3);
        addEdge(499, 402, 1);
        addEdge(500, 227, 1);
        addEdge(58, 205, 5);
        addEdge(123, 356, 3);
        addEdge(285, 9, 6);
        addEdge(215, 415, 4);
        addEdge(85, 375, 5);
        addEdge(99, 117, 5);
        addEdge(401, 1, 5);
        addEdge(463, 314, 4);
        addEdge(16, 482, 3);
        addEdge(269, 363, 1);
        addEdge(177, 351, 1);
        addEdge(336, 231, 1);
        addEdge(259, 479, 5);
        addEdge(218, 1, 3);
        addEdge(53, 252, 1);
        addEdge(437, 496, 6);
        addEdge(75, 47, 6);
        addEdge(383, 38, 4);
        addEdge(335, 38, 2);
        addEdge(105, 269, 1);
        addEdge(20, 337, 2);
        addEdge(393, 219, 5);
        addEdge(234, 191, 2);
        addEdge(238, 420, 6);
        addEdge(177, 138, 3);
        addEdge(115, 30, 6);
        addEdge(474, 125, 4);
        addEdge(250, 248, 1);
        addEdge(341, 469, 3);
        addEdge(457, 416, 2);
        addEdge(468, 417, 5);
        addEdge(403, 170, 5);
        addEdge(5, 25, 1);
        addEdge(163, 215, 5);
        addEdge(174, 168, 3);
        addEdge(163, 414, 4);
        addEdge(465, 55, 2);
        addEdge(164, 431, 1);
        addEdge(110, 157, 4);
        addEdge(114, 492, 1);
        addEdge(165, 405, 6);
        addEdge(189, 117, 6);
        addEdge(195, 252, 5);
        addEdge(175, 298, 1);
        addEdge(19, 435, 5);
        addEdge(330, 416, 4);
        addEdge(332, 210, 2);
        addEdge(460, 181, 5);
        addEdge(273, 167, 1);
        addEdge(65, 286, 2);
        addEdge(477, 181, 2);
        addEdge(60, 54, 4);
        addEdge(500, 83, 3);
        addEdge(260, 293, 4);
        addEdge(299, 242, 4);
        addEdge(278, 38, 2);
        addEdge(357, 338, 4);
        addEdge(467, 98, 5);
        addEdge(464, 308, 1);
        addEdge(383, 473, 1);
        addEdge(69, 172, 5);
        addEdge(448, 144, 4);
        addEdge(466, 245, 3);
        addEdge(273, 467, 6);
        addEdge(72, 413, 3);
        addEdge(114, 66, 3);
        addEdge(21, 389, 3);
        addEdge(173, 1, 3);
        addEdge(388, 448, 1);
        addEdge(112, 368, 1);
        addEdge(278, 492, 3);
        addEdge(408, 262, 3);
        addEdge(386, 405, 2);
        addEdge(49, 448, 6);
        addEdge(222, 150, 5);
        addEdge(433, 97, 4);
        addEdge(162, 319, 6);
        addEdge(434, 398, 3);
        addEdge(475, 434, 3);
        addEdge(5, 165, 2);
        addEdge(278, 73, 6);
        addEdge(412, 428, 4);
        addEdge(138, 150, 6);
        addEdge(438, 257, 1);
        addEdge(132, 406, 5);
        addEdge(308, 139, 3);
        addEdge(481, 168, 5);
        addEdge(417, 75, 2);
        addEdge(55, 88, 2);
        addEdge(212, 307, 3);
        addEdge(206, 154, 4);
        addEdge(215, 19, 2);
        addEdge(273, 188, 4);
        addEdge(433, 101, 2);
        addEdge(302, 156, 2);
        addEdge(91, 327, 6);
        addEdge(174, 355, 1);
        addEdge(284, 233, 4);
        addEdge(384, 263, 2);
        addEdge(492, 110, 2);
        addEdge(409, 114, 5);
        addEdge(134, 90, 5);
        addEdge(206, 429, 3);
        addEdge(499, 458, 1);
        addEdge(245, 457, 4);
        addEdge(361, 129, 6);
        addEdge(122, 429, 6);
        addEdge(167, 99, 4);
        addEdge(261, 223, 5);
        addEdge(452, 97, 1);
        addEdge(189, 198, 4);
        addEdge(125, 489, 4);
        addEdge(342, 347, 6);
        addEdge(165, 143, 6);
        addEdge(255, 233, 2);
        addEdge(338, 380, 6);
        addEdge(18, 254, 1);
        addEdge(220, 285, 1);
        addEdge(463, 71, 3);
        addEdge(147, 371, 3);
        addEdge(195, 105, 2);
        addEdge(381, 377, 3);
        addEdge(422, 405, 4);
        addEdge(475, 328, 6);
        addEdge(396, 250, 2);
        addEdge(364, 270, 6);
        addEdge(299, 207, 2);
        addEdge(417, 488, 2);
        addEdge(136, 472, 4);
        addEdge(297, 237, 3);
        addEdge(375, 28, 4);
        addEdge(342, 175, 5);
        addEdge(252, 483, 2);
        addEdge(223, 97, 3);
        addEdge(385, 487, 2);
        addEdge(131, 169, 3);
        addEdge(52, 121, 3);
        addEdge(372, 44, 6);
        addEdge(363, 53, 5);
        addEdge(80, 52, 5);
        addEdge(181, 303, 1);
        addEdge(22, 275, 1);
        addEdge(107, 79, 4);
        addEdge(435, 11, 5);
        addEdge(122, 487, 4);
        addEdge(459, 29, 4);
        addEdge(132, 406, 6);
        addEdge(328, 354, 2);
        addEdge(302, 387, 2);
        addEdge(88, 41, 2);
        addEdge(113, 256, 1);
        addEdge(463, 211, 3);
        addEdge(143, 62, 1);
        addEdge(313, 229, 4);
        addEdge(173, 84, 6);
        addEdge(237, 174, 1);
        addEdge(45, 251, 5);
        addEdge(355, 278, 6);
        addEdge(446, 434, 5);
        addEdge(167, 324, 6);
        addEdge(157, 124, 4);
        addEdge(452, 124, 4);
        addEdge(401, 104, 1);
        addEdge(363, 450, 6);
        addEdge(118, 286, 6);
        addEdge(3, 28, 6);
        addEdge(54, 99, 2);
        addEdge(160, 167, 2);
        addEdge(195, 158, 4);
        addEdge(499, 218, 4);
        addEdge(284, 104, 5);
        addEdge(153, 118, 6);
        addEdge(330, 432, 4);
        addEdge(142, 82, 5);
        addEdge(173, 414, 1);
        addEdge(96, 173, 3);
        addEdge(19, 200, 4);
        addEdge(282, 454, 4);
        addEdge(4, 35, 1);
        addEdge(109, 349, 3);
        addEdge(194, 341, 1);
        addEdge(390, 205, 1);
        addEdge(153, 171, 2);
        addEdge(478, 182, 6);
        addEdge(430, 292, 2);
        addEdge(149, 336, 5);
        addEdge(312, 472, 1);
        addEdge(462, 240, 4);
        addEdge(418, 352, 3);
        addEdge(77, 44, 4);
        addEdge(280, 244, 5);
        addEdge(158, 468, 4);
        addEdge(268, 297, 6);
        addEdge(135, 384, 2);
        addEdge(79, 388, 6);
        addEdge(38, 345, 5);
        addEdge(9, 340, 5);
        addEdge(12, 111, 5);
        addEdge(401, 414, 6);
        addEdge(67, 300, 6);
        addEdge(286, 434, 6);
        addEdge(294, 318, 4);
        addEdge(356, 262, 6);
        addEdge(426, 40, 6);
        addEdge(124, 278, 5);
        addEdge(351, 149, 1);
        addEdge(315, 266, 6);
        addEdge(126, 357, 5);
        addEdge(246, 306, 6);
        addEdge(471, 214, 2);
        addEdge(241, 66, 2);
        addEdge(116, 491, 3);
        addEdge(118, 22, 2);
        addEdge(146, 339, 4);
        addEdge(437, 82, 2);
        addEdge(56, 464, 3);
        addEdge(7, 307, 4);
        addEdge(151, 21, 1);
        addEdge(419, 131, 6);
        addEdge(376, 25, 2);
        addEdge(371, 444, 4);
        addEdge(138, 173, 2);
        addEdge(117, 266, 5);
        addEdge(122, 276, 6);
        addEdge(394, 243, 4);
        addEdge(78, 336, 3);
        addEdge(490, 382, 5);
        addEdge(305, 148, 2);
        addEdge(175, 486, 5);
        addEdge(314, 124, 5);
        addEdge(257, 235, 6);
        addEdge(332, 451, 4);
        addEdge(434, 326, 2);
        addEdge(16, 26, 2);
        addEdge(314, 307, 6);
        addEdge(186, 323, 3);
        addEdge(330, 287, 1);
        addEdge(170, 53, 1);
        addEdge(257, 140, 4);
        addEdge(130, 201, 6);
        addEdge(330, 440, 3);
        addEdge(445, 35, 1);
        addEdge(319, 111, 2);
        addEdge(144, 117, 6);
        addEdge(284, 88, 3);
        addEdge(8, 17, 2);
        addEdge(82, 284, 3);
        addEdge(217, 453, 1);
        addEdge(58, 33, 3);
        addEdge(186, 487, 2);
        addEdge(490, 277, 3);
        addEdge(418, 94, 4);
        addEdge(81, 436, 1);
        addEdge(378, 466, 5);
        addEdge(243, 376, 4);
        addEdge(479, 440, 2);
        addEdge(437, 116, 1);
        addEdge(206, 289, 5);
        addEdge(13, 379, 4);
        addEdge(375, 393, 6);
        addEdge(450, 44, 3);
        addEdge(69, 402, 1);
        addEdge(169, 38, 5);
        addEdge(466, 234, 4);
        addEdge(211, 410, 5);
        addEdge(437, 371, 5);
        addEdge(489, 317, 3);
        addEdge(148, 201, 3);
        addEdge(399, 113, 6);
        addEdge(122, 408, 4);
        addEdge(243, 480, 1);
        addEdge(115, 290, 3);
        addEdge(3, 30, 2);
        addEdge(86, 434, 5);
        addEdge(54, 396, 6);
        addEdge(242, 117, 1);
        addEdge(250, 228, 2);
        addEdge(351, 279, 6);
        addEdge(432, 45, 3);
        addEdge(485, 411, 3);
        addEdge(28, 250, 4);
        addEdge(174, 38, 3);
        addEdge(462, 67, 1);
        addEdge(250, 418, 2);
        addEdge(451, 471, 1);
        addEdge(105, 412, 6);
        addEdge(290, 71, 4);
        addEdge(215, 466, 2);
        addEdge(225, 101, 3);
        addEdge(110, 309, 4);
        addEdge(278, 167, 1);
        addEdge(5, 138, 4);
        addEdge(17, 350, 2);
        addEdge(386, 129, 5);
        addEdge(135, 217, 5);
        addEdge(43, 203, 3);
        addEdge(123, 301, 3);
        addEdge(109, 338, 6);
        addEdge(158, 12, 1);
        addEdge(275, 284, 1);
        addEdge(466, 333, 3);
        addEdge(324, 313, 3);
        addEdge(333, 181, 3);
        addEdge(344, 176, 6);
        addEdge(91, 326, 6);
        addEdge(476, 294, 6);
        addEdge(354, 339, 3);
        addEdge(427, 151, 4);
        addEdge(340, 263, 4);
        addEdge(459, 20, 1);
        addEdge(385, 467, 3);
        addEdge(407, 199, 6);
        addEdge(462, 13, 5);
        addEdge(6, 375, 6);
        addEdge(59, 123, 1);
        addEdge(195, 142, 1);
        addEdge(368, 110, 3);
        addEdge(194, 234, 1);
        addEdge(43, 102, 1);
        addEdge(348, 245, 4);
        addEdge(245, 400, 1);
        addEdge(237, 181, 3);
        addEdge(271, 132, 2);
        addEdge(463, 419, 6);
        addEdge(211, 182, 5);
        addEdge(58, 406, 5);
        addEdge(376, 113, 3);
        addEdge(252, 498, 1);
        addEdge(425, 304, 4);
        addEdge(429, 356, 2);
        addEdge(456, 144, 1);
        addEdge(107, 140, 1);
        addEdge(446, 428, 6);
        addEdge(280, 306, 3);
        addEdge(183, 83, 3);
        addEdge(18, 428, 4);
        addEdge(260, 17, 3);
        addEdge(456, 330, 6);
        addEdge(193, 228, 1);
        addEdge(461, 495, 1);
        addEdge(316, 152, 5);
        addEdge(286, 47, 3);
        addEdge(423, 354, 1);
        addEdge(355, 81, 2);
        addEdge(269, 221, 5);
        addEdge(67, 34, 4);
        addEdge(251, 87, 3);
        addEdge(479, 259, 5);
        addEdge(115, 160, 3);
        addEdge(109, 197, 3);
        addEdge(15, 216, 3);
        addEdge(1, 175, 1);
        addEdge(26, 23, 3);
        addEdge(48, 99, 6);
        addEdge(299, 348, 2);
        addEdge(288, 453, 6);
        addEdge(94, 427, 2);
        addEdge(220, 466, 1);
        addEdge(161, 76, 1);
        addEdge(57, 330, 1);
        addEdge(353, 85, 5);
        addEdge(424, 163, 1);
        addEdge(345, 238, 3);
        addEdge(387, 275, 6);
        addEdge(435, 449, 5);
        addEdge(194, 389, 3);
        addEdge(203, 413, 3);
        addEdge(100, 131, 2);
        addEdge(145, 71, 2);
        addEdge(108, 426, 3);
        addEdge(384, 346, 6);
        addEdge(380, 98, 5);
        addEdge(445, 284, 3);
        addEdge(130, 405, 4);
        addEdge(375, 291, 1);
        addEdge(390, 77, 1);
        addEdge(477, 390, 6);
        addEdge(139, 135, 6);
        addEdge(411, 149, 5);
        addEdge(316, 133, 4);
        addEdge(174, 141, 1);
        addEdge(126, 424, 6);
        addEdge(254, 295, 4);
        addEdge(321, 197, 6);
        addEdge(494, 311, 4);
        addEdge(134, 441, 3);
        addEdge(5, 62, 5);
        addEdge(147, 370, 6);
        addEdge(155, 250, 3);
        addEdge(353, 370, 5);
        addEdge(69, 417, 6);
        addEdge(353, 79, 3);
        addEdge(30, 49, 1);
        addEdge(130, 481, 2);
        addEdge(304, 328, 4);
        addEdge(361, 17, 2);
        addEdge(324, 275, 5);
        addEdge(415, 322, 3);
        addEdge(118, 238, 3);
        addEdge(424, 129, 6);
        addEdge(283, 461, 4);
        addEdge(87, 134, 6);
        addEdge(212, 359, 1);
        addEdge(369, 468, 6);
        addEdge(489, 141, 3);
        addEdge(85, 347, 4);
        addEdge(422, 27, 2);
        addEdge(356, 237, 2);
        addEdge(393, 453, 3);
        addEdge(398, 485, 5);
        addEdge(154, 212, 4);
        addEdge(353, 409, 6);
        addEdge(457, 262, 2);
        addEdge(406, 256, 3);
        addEdge(451, 294, 2);
        addEdge(390, 20, 1);
        addEdge(247, 26, 1);
        addEdge(487, 364, 5);
        addEdge(499, 324, 4);
        addEdge(140, 62, 1);
        addEdge(369, 202, 1);
        addEdge(281, 238, 1);
        addEdge(476, 58, 1);
        addEdge(78, 38, 2);
        addEdge(109, 228, 6);
        addEdge(109, 197, 5);
        addEdge(337, 427, 6);
        addEdge(381, 313, 4);
        addEdge(350, 163, 6);
        addEdge(3, 125, 4);
        addEdge(399, 409, 6);
        addEdge(251, 408, 6);
        addEdge(49, 371, 5);
        addEdge(376, 253, 2);
        addEdge(38, 49, 6);
        addEdge(189, 150, 3);
        addEdge(310, 204, 4);
        addEdge(212, 139, 5);
        addEdge(38, 449, 3);
        addEdge(333, 431, 4);
        addEdge(352, 288, 1);
        addEdge(492, 266, 4);
        addEdge(447, 115, 2);
        addEdge(312, 241, 3);
        addEdge(444, 334, 1);
        addEdge(293, 131, 3);
        addEdge(233, 500, 1);
        addEdge(435, 162, 2);
        addEdge(103, 330, 2);
        addEdge(231, 38, 6);
        addEdge(61, 290, 1);
        addEdge(244, 115, 6);
        addEdge(220, 353, 6);
        addEdge(448, 341, 6);
        addEdge(395, 72, 4);
        addEdge(259, 337, 1);
        addEdge(499, 35, 6);
        addEdge(449, 95, 3);
        addEdge(450, 449, 2);
        addEdge(398, 364, 5);
        addEdge(395, 348, 1);
        addEdge(207, 348, 5);
        addEdge(314, 275, 6);
        addEdge(444, 337, 6);
        addEdge(36, 377, 2);
        addEdge(449, 409, 1);
        addEdge(228, 297, 5);
        addEdge(231, 485, 5);
        addEdge(456, 484, 6);
        addEdge(176, 422, 2);
        addEdge(327, 294, 5);
        addEdge(282, 497, 5);
        addEdge(393, 254, 3);
        addEdge(389, 127, 3);
        addEdge(199, 244, 2);
        addEdge(285, 158, 3);
        addEdge(465, 30, 3);
        addEdge(273, 334, 4);
        addEdge(250, 65, 4);
        addEdge(103, 358, 3);
        addEdge(369, 200, 5);
        addEdge(49, 154, 5);
        addEdge(101, 182, 2);
        addEdge(343, 389, 6);
        addEdge(160, 119, 2);
        addEdge(9, 100, 4);
        addEdge(269, 148, 4);
        addEdge(152, 386, 5);
        addEdge(212, 123, 6);
        addEdge(285, 376, 3);
        addEdge(156, 259, 4);
        addEdge(273, 469, 4);
        addEdge(460, 164, 6);
        addEdge(170, 264, 2);
        addEdge(307, 119, 6);
        addEdge(473, 437, 5);
        addEdge(104, 65, 1);
        addEdge(446, 315, 6);
        addEdge(496, 81, 4);
        addEdge(308, 265, 4);
    }

    public void addSmallGraph1() {
        addEdge(7, 3, 3);
        addEdge(10, 9, 3);
        addEdge(9, 3, 2);
        addEdge(3, 5, 2);
        addEdge(5, 4, 4);
        addEdge(9, 6, 2);
        addEdge(7, 5, 3);
        addEdge(5, 10, 6);
        addEdge(10, 4, 5);
        addEdge(2, 3, 2);
        addEdge(2, 7, 3);
        addEdge(8, 2, 6);
        addEdge(3, 1, 4);
        addEdge(1, 8, 4);
        addEdge(2, 9, 2);
        addEdge(6, 1, 3);
        addEdge(3, 10, 5);
        addEdge(1, 9, 3);
        addEdge(5, 6, 2);
        addEdge(8, 7, 4);
    }

    public void addSmallGraph2() {
        addEdge(1, 2, 3);
        addEdge(1, 3, 7);
        addEdge(2, 4, 3);
        addEdge(2, 5, 3);
        addEdge(2, 6, 9);
        addEdge(7, 8, 2);
        addEdge(7, 3, 5);
        addEdge(4, 5, 2);
        addEdge(5, 3, 2);
        addEdge(3, 9, 3);
        addEdge(3, 10, 4);
        addEdge(11, 9, 6);
        addEdge(11, 12, 5);
        addEdge(13, 8, 3);
        addEdge(8, 14, 6);
        addEdge(9, 15, 3);
        addEdge(9, 16, 5);
        addEdge(16, 5, 4);
        addEdge(10, 11, 3);
        addEdge(10, 17, 4);
        addEdge(15, 2, 3);
        addEdge(18, 12, 2);
        addEdge(19, 18, 7);
        addEdge(20, 21, 2);
        addEdge(20, 19, 4);
        addEdge(22, 23, 3);
        addEdge(22, 14, 4);
        addEdge(24, 22, 6);
        addEdge(23, 25, 4);
        addEdge(23, 17, 2);
        addEdge(26, 27, 6);
        addEdge(26, 19, 6);
        addEdge(26, 30, 5);
        addEdge(14, 11, 6);
        addEdge(14, 10, 4);
        addEdge(27, 28, 4);
        addEdge(12, 21, 2);
        addEdge(12, 29, 5);
        addEdge(21, 9, 5);
        addEdge(30, 29, 2);
        addEdge(28, 30, 2);
        addEdge(25, 29, 3);
        addEdge(30, 4, 3);
        addEdge(12, 30, 4);
        addEdge(17, 20, 3);
        addEdge(17, 12, 3);
        addEdge(30, 21, 6);
        addEdge(29, 7, 3);
        addEdge(11, 29, 4);
        addEdge(20, 30, 8);
    }

}
