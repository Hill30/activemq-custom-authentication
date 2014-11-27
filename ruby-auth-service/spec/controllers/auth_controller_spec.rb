require 'rails_helper'

RSpec.describe AuthController, :type => :controller do

  describe "GET auth" do
    it "returns http success" do
      get :auth, {username: 'admin', password: 'admin'}
      expect(response).to have_http_status(:success)
    end
    it "returns http error" do
      get :auth, {username: 'admin', password: 'wrong'}
      expect(response).to have_http_status(:forbidden)
    end
  end

end
