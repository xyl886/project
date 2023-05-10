const TokenKey = 'my_token'
const userInfoKey = 'userInfo'

export function getToken () {
  return localStorage.getItem(TokenKey)
}

export function setToken (token) {
  return localStorage.setItem(TokenKey, token)
}

export function removeToken () {
  localStorage.removeItem(userInfoKey)
  return localStorage.removeItem(TokenKey)
}
