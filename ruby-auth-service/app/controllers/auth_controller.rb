class AuthController < ApplicationController
  def auth
    username = params[:username]
    password = params[:password]
    creds = {
      'admin' => 'admin',
      'userName' => 'password',
    }
    if creds[username] == password && !password.nil?
      render status: :ok, json: {notice: 'Login OK'}
    else
      render status: :forbidden, json: {notice: 'Wrong pair login-password'}
    end
  end
end
