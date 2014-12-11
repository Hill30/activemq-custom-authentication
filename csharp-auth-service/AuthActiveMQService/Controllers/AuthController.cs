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
        private HttpResponseMessage Authenticate(string username, string password)
        {
            // replace with real validation
            var creds = new Dictionary<string, string>
            {
                {"admin", "admin"},
                {"userName", "password"},
            };

            if (username != null && creds.ContainsKey(username) && creds[username] == password)
            {
                return this.Request.CreateResponse(HttpStatusCode.OK, "Login OK");
            }
            else
            {
                return this.Request.CreateResponse(HttpStatusCode.Forbidden, "Wrong pair login-password");
            }
        }

        public HttpResponseMessage Get()
        {
            var nvps = this.Request.GetQueryNameValuePairs();
            var username = nvps.Where(x => x.Key == "username").Select(x => x.Value).FirstOrDefault();
            var password = nvps.Where(x => x.Key == "password").Select(x => x.Value).FirstOrDefault();

            return Authenticate(username, password);    
        }

        public HttpResponseMessage Post(Credentials creds)
        {
            return Authenticate(creds.Username, creds.Password);
        }

        public class Credentials
        {
            public string Username { get; set; }
            public string Password { get; set; }
        }
    }
}