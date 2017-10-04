using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Project3
{
    public partial class project3 : Form
    {
        
        public project3()
        {
            InitializeComponent();
        }

        //This makes it so the program won't waste time loading everything at once on start-up. It only loads what you wants, on demand.
        //This also makes it so when a tab is loaded, it doesn't load again, causing... Interesting bugs due to the useage of appends instead of setting data.
        Boolean degreesVisited, minorsVisited, employmentVisited, peopleVisited, researchVisited, resourceVisited, newsVisited, footerVisited, coursesVisited;

        private void about_Enter(object sender, EventArgs e)
        {
            //No need to check if this tab have been visited because each time a request to the server for its about JSON file, random values are returned.
            populateAbout();
        }

        private void degrees_Enter(object sender, EventArgs e)
        {
            if (degreesVisited != true)
            {
                populateDegrees();
                degreesVisited = true;
            }   
        }

        private void minors_Enter(object sender, EventArgs e)
        {
            if(minorsVisited != true)
            {
                populateMinors();
                minorsVisited = true;
            }
        }

        private void employment_Enter(object sender, EventArgs e)
        {
            if(employmentVisited != true)
            {
                populateEmployment();
                employmentVisited = true;
            }
        }

        private void people_Enter(object sender, EventArgs e)
        {
            if(peopleVisited != true)
            {
                populatePeople();
                peopleVisited = true;
            }
        }

        private void research_Enter(object sender, EventArgs e)
        {
            if(researchVisited != true)
            {
                populateResearch();
                researchVisited = true;
            }
        }

        private void resources_Enter(object sender, EventArgs e)
        {
            if (resourceVisited != true)
            {
                populateResource();
                resourceVisited = true;
            }
        }

        private void news_Enter(object sender, EventArgs e)
        {
            if(newsVisited != true)
            {
                populateNews();
                newsVisited = true;
            }
        }

        private void footer_Enter(object sender, EventArgs e)
        {
            if(footerVisited != true)
            {
                populateFooter();
                footerVisited = true;
            }
        }

        private void courses_Enter(object sender, EventArgs e)
        {
            if(coursesVisited != true)
            {
                populateCourses();
                coursesVisited = true;
            }
        }

        public void populateAbout()
        {
            //Get /about/ info from API.
            string jsonAbout = getRESTData("/about/");

            //Rip data out of JSON string into an object form! That we can easily use.
            About about = JToken.Parse(jsonAbout).ToObject<About>();

            //ABOUT title
            aboutTitle.Text = about.title;

            //ABOUT Desc
            aboutDescription.Text = about.description;

            //Author
            aboutAuthor.Text = about.quoteAuthor;

            //Author quote
            aboutQuote.Text = about.quote;
        }

        public void populateDegrees()
        {
            //Ah, only if I could style this easily as I can with HTML5.
            string jsonDegree = getRESTData("/degrees/");

            Degrees degrees = JToken.Parse(jsonDegree).ToObject<Degrees>();

            //Loading up the info for undergrads!
            for (int i = 0; i < degrees.undergraduate.Count; i++)
            {
                string info = "";
                info += "Degree name: " + degrees.undergraduate[i].degreeName + "\r\n";
                info += "Title: " + degrees.undergraduate[i].title + "\r\n";
                info += "Description: " + degrees.undergraduate[i].description + "\r\n";

                if (degrees.undergraduate[i].concentrations != null)
                {
                    for (int j = 0; j < degrees.undergraduate[i].concentrations.Count; j++)
                        info += "Concentrations " + (j + 1) + ": " + degrees.undergraduate[i].concentrations[j] + "\r\n";
                }

                degreesUndergraduateText.AppendText(info + "\r\n");
            }

            //Loading up the info for grads!
            for (int i = 0; i < degrees.graduate.Count; i++)
            {
                string info = "";
                info += "Degree name: " + degrees.graduate[i].degreeName + "\r\n";

                //The API documentions are... Well, not accurate. The fourth item in graduate[] doesn't have title and description. Only availableCertificates.
                if (degrees.graduate[i].availableCertificates == null)
                {
                    info += "Title: " + degrees.graduate[i].title + "\r\n";
                    info += "Description: " + degrees.graduate[i].description + "\r\n";
                }
                else
                {
                    info += "Available Certificates\r\n" ;
                    for (int j = 0; j < degrees.graduate[i].availableCertificates.Count; j++)
                    {
                        info += "Certificate " + (j + 1) + ": " + degrees.graduate[i].availableCertificates[j] + "\r\n";
                    }
                }

                if (degrees.graduate[i].concentrations != null)
                {
                    for (int j = 0; j < degrees.graduate[i].concentrations.Count; j++)
                        info += "Concentrations " + (j + 1) + ": " + degrees.graduate[i].concentrations[j] + "\r\n";
                }

                degreesGraduateText.AppendText(info + "\r\n");
            }
        }

        public void populateMinors()
        {
            string jsonMinors = getRESTData("/minors/");

            Minors minors = JToken.Parse(jsonMinors).ToObject<Minors>();

            for(int i = 0; i < minors.UgMinors.Count; i++)
            {
                string info = "";
                info += "Degree Name: " + minors.UgMinors[i].name + "\r\n";
                info += "Title: " + minors.UgMinors[i].title + "\r\n";
                info += "Description: " + minors.UgMinors[i].description + "\r\n";

                info += "Courses:\r\n";
                for (int j = 0; j < minors.UgMinors[i].courses.Count; j++)
                    info += "\t" + minors.UgMinors[i].courses[j] + "\r\n";

                info += "Note: " + minors.UgMinors[i].note + "\r\n";
                minorsText.AppendText(info + "\r\n");
            }

        }

        public void populateEmployment()
        {
            string jsonEmployment = getRESTData("/employment/");

            Employment employment = JToken.Parse(jsonEmployment).ToObject<Employment>(); 

            //Introduction
            string info = employment.introduction.title + "\r\n";
            for(int i = 0; i < employment.introduction.content.Count; i++)
            {
                info += "\r\n" + employment.introduction.content[i].title + ":\r\n";
                info += employment.introduction.content[i].description + "\r\n";
            }

            //Statistics
            info += "\r\n" + employment.degreeStatistics.title + "\r\n";
            for(int i = 0; i < employment.degreeStatistics.statistics.Count; i++)
                info += employment.degreeStatistics.statistics[i].value + " " + employment.degreeStatistics.statistics[i].description + "\r\n";

            //Employers
            info += "\r\n" + employment.employers.title + "\r\n";
            for(int i = 0; i < employment.employers.employerNames.Count; i++)
                info += (i + 1) + ": " + employment.employers.employerNames[i] + "\r\n";

            //Careers
            info += "\r\n" + employment.careers.title + "\r\n";
            for(int i = 0; i < employment.careers.careerNames.Count; i++)
                info += (i + 1) + ": " + employment.careers.careerNames[i] + "\r\n";

            employmentText.AppendText(info);

            //Coop Gridview
            coopGridView.Columns.Add("Employer", "Employer");
            coopGridView.Columns.Add("Degree", "Degree");
            coopGridView.Columns.Add("City", "City");
            coopGridView.Columns.Add("Term", "Term");
            for(int i = 0; i < employment.coopTable.coopInformation.Count; i++)
            {
                string employer = employment.coopTable.coopInformation[i].employer;
                string degree = employment.coopTable.coopInformation[i].degree;
                string city = employment.coopTable.coopInformation[i].city;
                string term = employment.coopTable.coopInformation[i].term;
                coopGridView.Rows.Add(employer, degree, city, term);
            }

            //Employment Gridview
            employmentGridView.Columns.Add("Employer", "Employer");
            employmentGridView.Columns.Add("Degree", "Degree");
            employmentGridView.Columns.Add("City", "City");
            employmentGridView.Columns.Add("Title", "Title");
            employmentGridView.Columns.Add("StartDate", "StartDate");
            for(int i = 0; i < employment.employmentTable.professionalEmploymentInformation.Count; i++)
            {
                string employer = employment.employmentTable.professionalEmploymentInformation[i].employer;
                string degree = employment.employmentTable.professionalEmploymentInformation[i].degree;
                string city = employment.employmentTable.professionalEmploymentInformation[i].city;
                string title = employment.employmentTable.professionalEmploymentInformation[i].title;
                string startDate = employment.employmentTable.professionalEmploymentInformation[i].startDate;
                employmentGridView.Rows.Add(employer, degree, city, title, startDate);
            }
        }

        #region People -Region here because... Well... A bit more complex than the other tabs.
        int facultyCurrent = 0, staffCurrent = 0;
        List<Faculty> faculty;
        List<Staff> staff;

        public void populatePeople()
        {
            string jsonPeople = getRESTData("/people/");

            People people = JToken.Parse(jsonPeople).ToObject<People>();
            //This makes it so it's possible for me to use other methods to change the data based on the user input.
            faculty = people.faculty;
            staff = people.staff;

            //faculty defaults
            updateFaculty(facultyCurrent);

            //Staff defaults
            updateStaff(staffCurrent);
        }
        #region buttonPresses
        private void peopleFacultyPreviousbtn_Click(object sender, EventArgs e)
        {
            if (--facultyCurrent >= 0)
                updateFaculty(facultyCurrent);
            else
                facultyCurrent = 0;
        }

        private void peopleFacultyNextBtn_Click(object sender, EventArgs e)
        {
            if (++facultyCurrent < faculty.Count)
                updateFaculty(facultyCurrent);
            else
                facultyCurrent = faculty.Count - 1;
        }

        private void peopleStaffPreviousBtn_Click(object sender, EventArgs e)
        {
            if (--staffCurrent >= 0)
                updateStaff(staffCurrent);
            else
                staffCurrent = 0;
        }

        private void peopleStaffNextbtn_Click(object sender, EventArgs e)
        {
            if (++staffCurrent < staff.Count)
                updateStaff(staffCurrent);
            else
                staffCurrent = staff.Count - 1;
        }
        #endregion

        #region Methods for updating Faculty and Staff infomation on button presses.
        public void updateFaculty(int x)
        {
            String info = "Username: " + faculty[x].username + "\r\n";
            info += "Name: " + faculty[x].name + "\r\n";
            info += "Tagline: " + faculty[x].tagline + "\r\n";
            info += "Title: " + faculty[x].title + "\r\n";
            info += "InterestArea: " + faculty[x].interestArea + "\r\n";
            info += "Office: " + faculty[x].office + "\r\n";
            info += "Website: " + faculty[x].website + "\r\n";
            info += "Phone: " + faculty[x].phone + "\r\n";
            info += "Email: " + faculty[x].email + "\r\n";
            info += "Twitter: " + faculty[x].twitter + "\r\n";
            info += "Facebook: " + faculty[x].facebook + "\r\n";

            peopleFacultyText.Text = info;

            peopleFacultyPicture.ImageLocation = faculty[x].imagePath;

        }

        public void updateStaff(int x)
        {
            String info = "Username: " + staff[x].username + "\r\n";
            info += "Name: " + staff[x].name + "\r\n";
            info += "Tagline: " + staff[x].tagline + "\r\n";
            info += "Title: " + staff[x].title + "\r\n";
            info += "InterestArea: " + staff[x].interestArea + "\r\n";
            info += "Office: " + staff[x].office + "\r\n";
            info += "Website: " + staff[x].website + "\r\n";
            info += "Phone: " + staff[x].phone + "\r\n";
            info += "Email: " + staff[x].email + "\r\n";
            info += "Twitter: " + staff[x].twitter + "\r\n";
            info += "Facebook: " + staff[x].facebook + "\r\n";

            peopleStaffText.Text = info;

            peopleStaffPicture.ImageLocation = staff[x].imagePath;
        }
        #endregion
        #endregion

        #region Research-Region here for the same reason as People. Complex.
        int interestAreaCurrent = 0, facultyCurrentPerson = 1; //Don't ask why currentPerson = 1. Code too complex now to bother explaining that.
        List<ByInterestArea> InterestArea; //Bad naming here, yes. Just couldn't think of a better name at the moment. More important things to do.
        List<ByFaculty> Faculty;
        public void populateResearch()
        {
            string jsonResearch = getRESTData("/research/");

            Research research = JToken.Parse(jsonResearch).ToObject<Research>();

            //By Interest Area
            InterestArea = research.byInterestArea;
            updateInterestArea(interestAreaCurrent);

            //By Faculty.
            Faculty = research.byFaculty;
            updateResearchByFaculty(facultyCurrentPerson);

        }
        #region Interest Area buttons and stuff.
        private void researchByInterestAreaPrevious_Click(object sender, EventArgs e)
        {
            if (--interestAreaCurrent >= 0)
                updateInterestArea(interestAreaCurrent);
            else
                interestAreaCurrent = 0;
        }

        private void researchByInterestAreaNext_Click(object sender, EventArgs e)
        {
            if (++interestAreaCurrent < InterestArea.Count)
                updateInterestArea(interestAreaCurrent);
            else
                interestAreaCurrent = InterestArea.Count - 1;
        }

        private void updateInterestArea(int x)
        {
            string info = InterestArea[x].areaName + "\r\n\r\n";
            for (int i = 0; i < InterestArea[x].citations.Count; i++)
                info += InterestArea[x].citations[i] + "\r\n\r\n";

            researchByInterestAreaText.Text = info;
            researchByInterestAreaLabel.Text = InterestArea[x].areaName;
        }
        #endregion
        #region By Faculty buttons and stuff.
        private void researchByFacltyPrevious_Click(object sender, EventArgs e)
        {
            if (--facultyCurrentPerson >= 1)
                updateResearchByFaculty(facultyCurrentPerson);
            else
                facultyCurrentPerson = 1;
        }

        private void researchByFacultyNext_Click(object sender, EventArgs e)
        {
            if (++facultyCurrentPerson < Faculty.Count)
                updateResearchByFaculty(facultyCurrentPerson);
            else
                facultyCurrentPerson = Faculty.Count -1;
        }

        private void updateResearchByFaculty(int x)
        {
            //Need one of the lists from this tab to make it easy to work in a next/previous buttons...
            if(peopleVisited != true)
            {
                populatePeople();
                peopleVisited = true;
            }

            for(int i = 0; i < faculty.Count; i++)
                if(Faculty[x].username.Equals(faculty[i].username)) //I uh... I keep track by the types by looking at the type of the list via the tooltip.
                    researchByFacultyPicture.ImageLocation = faculty[i].imagePath;

            researchByFacultyInfo.Text = "Name: " + Faculty[x].facultyName + "\r\n" + "Username: " + Faculty[x].username;

            string info = "Citations\r\n\r\n";
            for (int i = 0; i < Faculty[x].citations.Count; i++)
                 info += Faculty[x].citations[i] + "\r\n\r\n";

            researchByFacultyText.Text = info;

        }
        #endregion
        #endregion

        public void populateResource()
        {
            //Add resources link to job zone.
            string jsonResource = getRESTData("/resources/");

            Resources resources = JToken.Parse(jsonResource).ToObject<Resources>();
            resourcesTitle.Text = resources.title;
            resourcesSubTitle.Text = resources.subTitle;

            string info = resources.studyAbroad.title + "\r\n\r\n";
            info += resources.studyAbroad.description;
            resourcesStudyText.Text = info;
            info = "";

            for (int i = 0; i < resources.studyAbroad.places.Count; i++)
                info += resources.studyAbroad.places[i].nameOfPlace + "\r\n\r\n" + resources.studyAbroad.places[i].description + "\r\n\r\n";
            resourcesPlacesText.Text = info;
            

            AcademicAdvisorText.Text = resources.studentServices.academicAdvisors.description + "\r\n\r\n" + resources.studentServices.academicAdvisors.faq.title + "\r\n\r\n" + resources.studentServices.academicAdvisors.faq.contentHref;

            info = resources.studentServices.professonalAdvisors.title + "\r\n\r\n";
            for (int i = 0; i < resources.studentServices.professonalAdvisors.advisorInformation.Count; i++)
            {
                info += "Name: " + resources.studentServices.professonalAdvisors.advisorInformation[i].name + "\r\n";
                info += "Email: " + resources.studentServices.professonalAdvisors.advisorInformation[i].email + "\r\n";
                info += "Department: " + resources.studentServices.professonalAdvisors.advisorInformation[i].department + "\r\n\r\n";
            }
            professonalAdvisorsText.Text = info;

            info = resources.studentServices.facultyAdvisors.title + "\r\n\r\n";
            info += resources.studentServices.facultyAdvisors.description + "\r\n\r\n";
            facultyAdvisorsText.Text = info;

            info = resources.studentServices.istMinorAdvising.title + "\r\n\r\n";
            for (int i = 0; i < resources.studentServices.istMinorAdvising.minorAdvisorInformation.Count; i++)
                info += "Title: " + resources.studentServices.istMinorAdvising.minorAdvisorInformation[i].title + "\r\nEmail: " + resources.studentServices.istMinorAdvising.minorAdvisorInformation[i].email + "\r\nAdvisor:" + resources.studentServices.istMinorAdvising.minorAdvisorInformation[i].advisor + "\r\n\r\n";

            MinorAdvisingText.Text = info;

            info = resources.tutorsAndLabInformation.title + "\r\n\r\n";
            info += resources.tutorsAndLabInformation.description + "\r\n\r\n";
            info += resources.tutorsAndLabInformation.tutoringLabHoursLink;

            TutorsAndLabInfoText.Text = info;

            info = resources.studentAmbassadors.title + "\r\n\r\n";
            ambassadorsPicture.ImageLocation = resources.studentAmbassadors.ambassadorsImageSource;
            for(int i = 0; i < resources.studentAmbassadors.subSectionContent.Count; i++)
            {
                info += resources.studentAmbassadors.subSectionContent[i].title + "\r\n";
                info += resources.studentAmbassadors.subSectionContent[i].description + "\r\n\r\n";
            }
            info += resources.studentAmbassadors.applicationFormLink + "\r\n\r\n";
            info += resources.studentAmbassadors.note;

            studentAmbassadorText.Text = info;

            info = "";
            for(int i = 0; i < resources.forms.graduateForms.Count; i++)
            {
                info += resources.forms.graduateForms[i].formName + "\r\n";
                info += resources.forms.graduateForms[i].href + "\r\n\r\n";
            }
            gradText.Text = info;

            info = "";
            for(int i = 0; i <resources.forms.undergraduateForms.Count; i++)
            {
                info += resources.forms.undergraduateForms[i].formName + "\r\n";
                info += resources.forms.undergraduateForms[i].href + "\r\n\r\n";
            }
            underGradText.Text = info;

            info = resources.coopEnrollment.title + "\r\n\r\n";
            for(int i = 0; i < resources.coopEnrollment.enrollmentInformationContent.Count; i++)
            {
                info += resources.coopEnrollment.enrollmentInformationContent[i].title + "\r\n";
                info += resources.coopEnrollment.enrollmentInformationContent[i].description + "\r\n\r\n";
            }
            info += resources.coopEnrollment.RITJobZoneGuidelink;
            coopEnrollText.Text = info;
        }

        public void populateNews()
        {
            string jsonNews = getRESTData("/news/");

            News news = JToken.Parse(jsonNews).ToObject<News>();

            string info = "";
            for(int i = 0; i < news.year.Count; i++)
            {
                info += news.year[i].title + "\r\n";
                info += news.year[i].date + "\r\n";
                info += news.year[i].description + "\r\n\r\n";
            }
            newsYearText.Text = info;

            info = "";
            for(int i = 0; i < news.older.Count; i++)
            {
                info += news.older[i].title + "\r\n";
                info += news.older[i].date + "\r\n";
                info += news.older[i].description + "\r\n\r\n";
            }
            newsOlderText.Text = info;
        }

        public void populateFooter()
        {
            string jsonFooter = getRESTData("/footer/");

            Footer footer = JToken.Parse(jsonFooter).ToObject<Footer>();
            footerNewsText.Text = footer.news;

            string info = "";
            info += footer.social.title + "\r\n";
            info += footer.social.tweet + "\r\n";
            info += footer.social.by + "\r\n";
            info += footer.social.twitter + "\r\n";
            info += footer.social.facebook + "\r\n";
            footerSocialText.Text = info;

            info = "";
            for(int i = 0; i < footer.quickLinks.Count; i++)
            {
                info += footer.quickLinks[i].title + "\r\n";
                info += footer.quickLinks[i].href + "\r\n\r\n";
            }
            footerQuickLinkText.Text = info;

            info = "";
            info += footer.copyright.title + "\r\n";
            info += footer.copyright.html + "\r\n";
            footerCopyrightText.Text = info;
        }

        public void populateCourses()
        {
            string jsonCourses = getRESTData("/courses/");
            
            //API/courses is a Java Array, not JSON. Have to parse it differently.
            JArray courses = JArray.Parse(jsonCourses);
            //This turns the JArray into an easy to use List with Courses objects in it. Magic!
            List<Courses> courseList = JsonConvert.DeserializeObject<List<Courses>>(courses.ToString());

            //Parsing the courses data and adding them to textboxes on the correct tabpages.
            foreach (TabPage page in tabControl11.TabPages)
            {
                foreach (Courses course in courseList)
                    if (course.degreeName.Equals(page.Text)) 
                    {
                        string info = "Degree name: " + course.degreeName + "\r\n";
                        info += "Semester: " + course.semester + "\r\nCourses:\r\n";
                        for (int i = 0; i < course.courses.Count; i++)
                        {
                            //Filtering out trash data.
                            //If there's no - in the string, it's ignored.
                            //If there's more than one - in the string, it's ignored.
                            if (course.courses[i].IndexOf("-") != -1 && course.courses[i].Count(x => x == '-') == 1) 
                                info += "\t" + course.courses[i] + "\r\n";
                        }
                        page.Controls[0].Text = info; //Easier than manually typing in the textbox's name each time! Just access each tabpage's first children, which is the textboxes!
                    }
            }
        }

        #region Common method to get JSON data from APIs.
        //Get dat REST info from API. 
        private string getRESTData(string url)
        {
            //The place where the JSON files can be found.
            const string baseURL = "http://ist.rit.edu/api";

            //Connect to the API
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(baseURL + url);
            try
            {
                //Wait and get the response for this web requewst.
                WebResponse response = request.GetResponse();
                // Using the response stream from the web requst
                //Get the infomation requested
                using (Stream reponseStream = response.GetResponseStream())
                {
                    StreamReader reader = new StreamReader(reponseStream, Encoding.UTF8);
                    return reader.ReadToEnd();
                }
            }
            catch (WebException error)
            {
                //GEt the error.
                WebResponse err = error.Response;
                using (Stream reponseStream = err.GetResponseStream())
                {
                    StreamReader r = new StreamReader(reponseStream, Encoding.UTF8);
                    //Do something with the error.
                    string errorText = r.ReadToEnd();
                    Console.WriteLine("ERROR: " + errorText);
                }
                //Nothing more to do with this problem.
                //Make it someone else's problem.
                throw;
            }
            throw new NotImplementedException();
        }//End getRESTData.
        #endregion
    }
}
