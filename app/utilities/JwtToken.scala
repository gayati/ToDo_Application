package utilities

import io.jsonwebtoken._
import javax.inject.Singleton
import java.util.Date

@Singleton
class JwtToken {

  def generateToken(id: Int): String = {

    val currentDate = new Date()
    val expirationdate = new Date(System.currentTimeMillis() + 24*60*60*1000)

    val Jwtbuilder = Jwts.builder().setId(Integer.toString(id))
      .setIssuedAt(currentDate)
      .setExpiration(expirationdate)
      .signWith(SignatureAlgorithm.HS256, "secretKey")
      .compact()
       return Jwtbuilder

  }
  
    def getTokenId(token: String) = {
    val claims = Jwts.parser().setSigningKey("secretKey").parseClaimsJws(token).getBody;
    var tokenId = Integer.parseInt(claims.getId)
    println(tokenId)
    tokenId
    }
  
  def generateLoginToken(id: Int,firstName:String,lastName:String,email:String): String = {

    val currentDate = new Date()
    val expirationdate = new Date(System.currentTimeMillis() + 24*60*60*1000)
    var fullName = firstName + " " + lastName
    val Jwtbuilder = Jwts.builder().setId(Integer.toString(id))
      .setSubject(email) 
			.setIssuer(fullName)
      .setIssuedAt(currentDate)
      .setExpiration(expirationdate)
      .signWith(SignatureAlgorithm.HS256, "secretKey")
      .compact()
       return Jwtbuilder

  }
  
  
  
  
  
  
  
  


}
      