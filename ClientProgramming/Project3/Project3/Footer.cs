using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Project3
{
    public class Social
    {
        public string title { get; set; }
        public string tweet { get; set; }
        public string by { get; set; }
        public string twitter { get; set; }
        public string facebook { get; set; }
    }

    public class QuickLink
    {
        public string title { get; set; }
        public string href { get; set; }
    }

    public class Copyright
    {
        public string title { get; set; }
        public string html { get; set; }
    }

    public class Footer
    {
        public Social social { get; set; }
        public List<QuickLink> quickLinks { get; set; }
        public Copyright copyright { get; set; }
        public string news { get; set; }
    }
}
