package za.unifiedza;

import java.util.List;
import static za.unifiedza.Institution.Course;

public final class InstitutionData {
    private InstitutionData() { }
    public static final List<Institution> ALL = List.of(
        new Institution("cput","Cape Peninsula University of Technology","CPUT","Western Cape","Cape Town","University of Technology",26,
            "Career-focused education, applied research and innovation across technology, business, design and applied sciences.",
            "CPUT offers career-focused programmes across technology, business, design, education and applied sciences. This profile demonstrates how applicants can explore institution information in one place.",
            List.of("Cape Town Campus","Bellville Campus","Mowbray Campus","Wellington Campus"),
            List.of("Informatics and Design","Engineering and the Built Environment","Business and Management Sciences","Applied Sciences","Education"),
            List.of(new Course("Diploma in Information Technology","Informatics and Design",28,"Mathematics or Mathematical Literacy"),new Course("Diploma in Multimedia Design","Informatics and Design",26,"English"),new Course("Diploma in Applications Development","Informatics and Design",28,"Mathematics"),new Course("Diploma in Business Administration","Business and Management Sciences",26,"English")),"https://www.cput.ac.za"),
        new Institution("uct","University of Cape Town","UCT","Western Cape","Cape Town","Traditional University",32,
            "A research-intensive university with a broad range of undergraduate programmes.","UCT is known for research, teaching and a wide range of academic disciplines. Applicants should review official faculty requirements.",
            List.of("Upper Campus","Middle Campus","Lower Campus","Health Sciences Campus"),List.of("Commerce","Engineering and the Built Environment","Health Sciences","Humanities","Law","Science"),
            List.of(new Course("BCom","Commerce",36,"Mathematics"),new Course("BSc Computer Science","Science",36,"Mathematics"),new Course("BEng","Engineering and the Built Environment",40,"Mathematics and Physical Sciences"),new Course("BA","Humanities",34,"English")),"https://www.uct.ac.za"),
        new Institution("uwc","University of the Western Cape","UWC","Western Cape","Bellville","Traditional University",28,
            "A public university committed to accessible education, research and community engagement.","UWC offers programmes in science, law, education, business and humanities. Programme-specific requirements must be confirmed.",
            List.of("Bellville Main Campus"),List.of("Arts and Humanities","Community and Health Sciences","Dentistry","Economic and Management Sciences","Education","Law","Natural Sciences"),
            List.of(new Course("BSc Computer Science","Natural Sciences",30,"Mathematics"),new Course("BCom","Economic and Management Sciences",28,"Mathematics or Mathematical Literacy"),new Course("LLB","Law",32,"English"),new Course("BEd","Education",28,"English")),"https://www.uwc.ac.za"),
        new Institution("sun","Stellenbosch University","SU","Western Cape","Stellenbosch","Traditional University",30,
            "A comprehensive university offering programmes across science, commerce, engineering and humanities.","Stellenbosch University provides a broad academic offering. Admission criteria differ between programmes and may include subject-specific minimums.",
            List.of("Stellenbosch Campus","Tygerberg Campus","Bellville Park Campus","Saldanha Campus"),List.of("AgriSciences","Arts and Social Sciences","Economic and Management Sciences","Engineering","Science"),
            List.of(new Course("BCom","Economic and Management Sciences",34,"Mathematics"),new Course("BSc","Science",34,"Mathematics and Physical Sciences"),new Course("BEng","Engineering",40,"Mathematics and Physical Sciences")),"https://www.sun.ac.za"),
        new Institution("up","University of Pretoria","UP","Gauteng","Pretoria","Traditional University",30,
            "A large public university with a diverse range of undergraduate and postgraduate programmes.","UP offers programmes across a wide range of faculties. Applicants should use official programme pages to confirm the latest admission requirements.",
            List.of("Hatfield Campus","Groenkloof Campus","Prinshof Campus"),List.of("Economic and Management Sciences","Engineering, Built Environment and IT","Humanities","Natural and Agricultural Sciences","Law"),
            List.of(new Course("BSc Computer Science","Engineering, Built Environment and IT",34,"Mathematics"),new Course("BCom","Economic and Management Sciences",32,"Mathematics"),new Course("BEng","Engineering, Built Environment and IT",35,"Mathematics and Physical Sciences")),"https://www.up.ac.za"),
        new Institution("ufs","University of the Free State","UFS","Free State","Bloemfontein","Traditional University",28,
            "A public university with campuses and programmes serving a diverse student community.","UFS offers qualifications across several faculties. Entry requirements are programme-specific and should be verified with the institution.",
            List.of("Bloemfontein Campus","Qwaqwa Campus","South Campus"),List.of("Economic and Management Sciences","Education","Health Sciences","Humanities","Natural and Agricultural Sciences"),
            List.of(new Course("BSc","Natural and Agricultural Sciences",30,"Mathematics"),new Course("BCom","Economic and Management Sciences",28,"Mathematics or Mathematical Literacy"),new Course("BA","Humanities",28,"English")),"https://www.ufs.ac.za")
    );
}
