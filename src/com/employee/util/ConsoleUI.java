package com.employee.util;

public class ConsoleUI {
	private ConsoleUI() {}

    public static final int WIDTH = 56;

    // ── Box characters ─────────────────────────────────────
    private static final String TL  = "+";
    private static final String TR  = "+";
    private static final String BL  = "+";
    private static final String BR  = "+";
    private static final String H   = "-";
    private static final String V   = "|";
    private static final String TM  = "+";
    private static final String BM  = "+";

    // ── Top / Bottom borders ────────────────────────────────
    public static String topBorder()    { return TL + repeat(H, WIDTH - 2) + TR; }
    public static String bottomBorder() { return BL + repeat(H, WIDTH - 2) + BR; }
    public static String divider()      { return TM + repeat(H, WIDTH - 2) + BM; }

    // ── Blank row ───────────────────────────────────────────
    public static String blankRow() { return V + repeat(" ", WIDTH - 2) + V; }

    // ── Centered text row ───────────────────────────────────
    public static String centered(String text) {
        int inner = WIDTH - 2;
        if (text.length() >= inner) return V + text.substring(0, inner) + V;
        int total = inner - text.length();
        int left  = total / 2;
        int right = total - left;
        return V + repeat(" ", left) + text + repeat(" ", right) + V;
    }

    // ── Left-aligned row with padding ───────────────────────
    public static String row(String text) {
        int inner = WIDTH - 4;
        String s = text.length() > inner ? text.substring(0, inner) : text;
        return V + " " + s + repeat(" ", inner - s.length()) + " " + V;
    }

    // ── Menu option row  e.g.  |  [1] Add Employee        | ─
    public static String option(String num, String label) {
        String text = "  [" + num + "] " + label;
        int inner = WIDTH - 4;
        if (text.length() > inner) text = text.substring(0, inner);
        return V + " " + text + repeat(" ", inner - text.length()) + " " + V;
    }

    // ── Print full header banner ────────────────────────────
    public static void printHeader(String title) {
        System.out.println();
        System.out.println(topBorder());
        System.out.println(blankRow());
        System.out.println(centered(title));
        System.out.println(blankRow());
        System.out.println(bottomBorder());
    }

    // ── Print section title ─────────────────────────────────
    public static void printSection(String title) {
        System.out.println();
        System.out.println(topBorder());
        System.out.println(centered("  " + title + "  "));
        System.out.println(divider());
    }

    // ── Print menu with options, then bottom border ─────────
    public static void printMenuEnd() {
        System.out.println(divider());
        System.out.println(option("0", "Back / Exit"));
        System.out.println(bottomBorder());
    }

    // ── Success / Error / Info messages ────────────────────
    public static void success(String msg) { System.out.println("\n  [OK] " + msg); }
    public static void error(String msg)   { System.out.println("\n  [!!] " + msg); }
    public static void info(String msg)    { System.out.println("  --> " + msg); }

    // ── Simple field display ────────────────────────────────
    public static void field(String label, String value) {
        System.out.printf("  %-18s: %s%n", label, value);
    }

    // ── Repeat a string n times ─────────────────────────────
    public static String repeat(String s, int n) {
        if (n <= 0) return "";
        StringBuilder sb = new StringBuilder(n * s.length());
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    // ── Print a full-width separator line ───────────────────
    public static void separator() {
        System.out.println(repeat("-", WIDTH));
    }
}
