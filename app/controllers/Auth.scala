package controllers

import tables.UserTable
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.Future

class Auth extends Controller with UserTable {
  lazy val authForm = Form(
    tuple(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    ))

  def signInForm = Action {
    Ok(views.html.sign_in_form(authForm))
  }

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def signIn = Action.async { implicit request =>
    authForm.bindFromRequest.fold(
      formWithErrors => Future(BadRequest(views.html.sign_in_form(formWithErrors))),
      user => {
        val (email, password) = user

        for (
          matchAmount <- authenticate(email, password)
          if matchAmount > 0
        ) yield Redirect(routes.Posts.index).withSession("email" -> email)
      })
  }
}
