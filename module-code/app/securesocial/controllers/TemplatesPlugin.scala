package securesocial.controllers

import play.api.mvc.{RequestHeader, Request}
import play.api.templates.Html
import play.api.{Logger, Plugin, Application}
import securesocial.core.SocialUser
import play.api.data.Form
import securesocial.controllers.Registration.RegistrationInfo

/**
 * A trait that defines methods that return the html pages and emails for SecureSocial.
 *
 * If you need to customise the views just create a new plugin
 * and register it instead of DefaultTemplatesPlugin in the play.plugins file of your app.
 *
 * @see DefaultViewsPlugins
 */
trait TemplatesPlugin extends Plugin {
  override def onStart() {
    Logger.info("Loaded templates plugin: %s".format(getClass.getName))
  }

  /**
   * Returns the html for the login page
   * @param request
   * @tparam A
   * @return
   */
  def getLoginPage[A](implicit request: Request[A], form: Form[(String, String)], msg: Option[String] = None): Html

  /**
   * Returns the html for the signup page
   *
   * @param request
   * @tparam A
   * @return
   */
  def getSignUpPage[A](implicit request: Request[A], form: Form[RegistrationInfo], token: String): Html

  /**
   * Returns the html for the start signup page
   *
   * @param request
   * @tparam A
   * @return
   */
  def getStartSignUpPage[A](implicit request: Request[A], form: Form[String]): Html

  /**
   * Returns the html for the reset password page
   *
   * @param request
   * @tparam A
   * @return
   */
  def getResetPasswordPage[A](implicit request: Request[A], form: Form[(String, String)], token: String): Html

  /**
   * Returns the html for the start reset page
   *
   * @param request
   * @tparam A
   * @return
   */
  def getStartResetPasswordPage[A](implicit request: Request[A], form: Form[String]): Html

  /**
   * Returns the email sent when a user starts the sign up process
   *
   * @param token the token used to identify the request
   * @param request the current http request
   * @return a String with the html code for the email
   */
  def getSignUpEmail(token: String)(implicit request: RequestHeader): String

  /**
   * Returns the email sent when the user is already registered
   *
   * @param user the user
   * @param request the current request
   * @return a String with the html code for the email
   */
  def getAlreadyRegisteredEmail(user: SocialUser)(implicit request: RequestHeader): String

  /**
   * Returns the welcome email sent when the user finished the sign up process
   *
   * @param user the user
   * @param request the current request
   * @return a String with the html code for the email
   */
  def getWelcomeEmail(user: SocialUser)(implicit request: RequestHeader): String

  /**
   * Returns the email sent when a user tries to reset the password but there is no account for
   * that email address in the system
   *
   * @param request the current request
   * @return a String with the html code for the email
   */
  def getUnknownEmailNotice()(implicit request: RequestHeader): String

  /**
   * Returns the email sent to the user to reset the password
   *
   * @param user the user
   * @param token the token used to identify the request
   * @param request the current http request
   * @return a String with the html code for the email
   */
  def getSendPasswordResetEmail(user: SocialUser, token: String)(implicit request: RequestHeader): String

  /**
   * Returns the email sent as a confirmation of a password change
   *
   * @param user the user
   * @param request the current http request
   * @return a String with the html code for the email
   */
  def getPasswordChangedNotice(user: SocialUser)(implicit request: RequestHeader): String

}

/**
 * The default views plugin.  If you need to customise the views just create a new plugin that
 * extends TemplatesPlugin and register it in the play.plugins file instead of this one.
 *
 * @param application
 */
class DefaultTemplatesPlugin(application: Application) extends TemplatesPlugin {
  override def getLoginPage[A](implicit request: Request[A], form: Form[(String, String)],
                               msg: Option[String] = None): Html =
  {
    securesocial.views.html.login(form, msg)
  }

  override def getSignUpPage[A](implicit request: Request[A], form: Form[RegistrationInfo], token: String): Html = {
    securesocial.views.html.Registration.signUp(form, token)
  }

  override def getStartSignUpPage[A](implicit request: Request[A], form: Form[String]): Html = {
    securesocial.views.html.Registration.startSignUp(form)
  }

  override def getStartResetPasswordPage[A](implicit request: Request[A], form: Form[String]): Html = {
    securesocial.views.html.Registration.startResetPassword(form)
  }

  def getResetPasswordPage[A](implicit request: Request[A], form: Form[(String, String)], token: String): Html = {
    securesocial.views.html.Registration.resetPasswordPage(form, token)
  }

  def getSignUpEmail(token: String)(implicit request: RequestHeader): String = {
    securesocial.views.html.mails.signUpEmail(token).body
  }

  def getAlreadyRegisteredEmail(user: SocialUser)(implicit request: RequestHeader): String = {
    securesocial.views.html.mails.alreadyRegisteredEmail(user).body
  }

  def getWelcomeEmail(user: SocialUser)(implicit request: RequestHeader): String = {
    securesocial.views.html.mails.welcomeEmail(user).body
  }

  def getUnknownEmailNotice()(implicit request: RequestHeader): String = {
    securesocial.views.html.mails.unknownEmailNotice(request).body
  }

  def getSendPasswordResetEmail(user: SocialUser, token: String)(implicit request: RequestHeader): String = {
    securesocial.views.html.mails.passwordResetEmail(user, token).body
  }

  def getPasswordChangedNotice(user: SocialUser)(implicit request: RequestHeader): String = {
    securesocial.views.html.mails.passwordChangedNotice(user).body
  }
}