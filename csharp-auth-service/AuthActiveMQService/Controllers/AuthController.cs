using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace AuthActiveMQService.Controllers
{
    public class AuthController : ApiController
    {
        // You need to implement only this POST method to make service work with ActiveMQ plug-in
        public HttpResponseMessage Post(Credentials creds)
        {
            return Authenticate(creds.Username, creds.Password);
        }

        // Helper that performs authentication
        private HttpResponseMessage Authenticate(string username, string password)
        {
            // replace with real user validation
            var usersRepo = new Dictionary<string, string>
            {
                {"admin", "admin"},
                {"userName", "password"},
            };

            if (username != null && usersRepo.ContainsKey(username) && usersRepo[username] == password)
                return Request.CreateResponse(HttpStatusCode.OK, "Login OK");

            return Request.CreateResponse(HttpStatusCode.Forbidden, "Wrong pair login-password");
        }

        // This method is only used for testing
        public HttpResponseMessage Get()
        {
            var nvps = this.Request.GetQueryNameValuePairs();
            var username = nvps.Where(x => x.Key == "username").Select(x => x.Value).FirstOrDefault();
            var password = nvps.Where(x => x.Key == "password").Select(x => x.Value).FirstOrDefault();

            return Authenticate(username, password);    
        }

        public class Credentials
        {
            public string Username { get; set; }
            public string Password { get; set; }
        }
    }
}