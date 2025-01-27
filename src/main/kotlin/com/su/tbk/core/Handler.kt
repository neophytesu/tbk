package com.su.tbk.core

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component


interface IAuthentication {
    fun getCurrentUsername(): String?
}

@Component
class AuthenticationHandler : IAuthentication {
    override fun getCurrentUsername(): String? {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null) {
            val principal = authentication.principal
            return if (principal is UserDetails) {
                principal.username
            } else {
                principal.toString()
            }
        }
        return null
    }

}