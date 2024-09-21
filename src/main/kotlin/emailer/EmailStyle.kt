package emailer

object EmailStyle {
    val tableStyle = """
            border-collapse: collapse;
            margin: 25px 0;
            font-family: sans-serif;
            min-width: 400px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    """.trimIndent()

    val thStyle = """
        background-color: #009879;
        color: #ffffff;
        text-align: left;
        border-bottom: 1px solid #dddddd;
        """

    val tdThStyle = "padding: 12px 15px;"
    val tdBorder = "border-bottom: 1px solid #dddddd;"
    val headerStyle = "color: #009879;"

    val linkStyle = """
            text-decoration: none;
            padding: 5px 5px;
            border-radius: 8px;
            font-family: sans-serif;
            text-align: center;
            color: white;
            background-color: #009879;
    """.trimIndent()
}