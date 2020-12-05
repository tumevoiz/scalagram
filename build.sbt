import Build._

lazy val root = Project(id = "scalagram", base = file(".")).aggregate()

lazy val common =
  Project(id = "common", base = file("common")).settings(allSettings: _*)

lazy val memberService = Project(
  id = "memberService",
  base = file("memberService")).settings(allSettings: _*).dependsOn(common)
