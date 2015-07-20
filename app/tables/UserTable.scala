package tables

import models.User
import slick.lifted.Tag
import slick.driver.PostgresDriver.api._

trait UserTable extends DBConnection {

  class Users(tag: Tag) extends Table[User](tag, "users") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def email = column[String]("email")

    def password = column[String]("password")

    def * = (id, email, password) <> ((User.apply _).tupled, User.unapply)
  }

  val users = TableQuery[Users]

  def getByEmailAndPassword(email: String, password: String) = {
    users.filter(u => u.email === email && u.password === password)
  }

  def authenticate(email: String, password: String) = db.run(
    users.filter(u => u.email === email && u.password === password).length.result
  )
}