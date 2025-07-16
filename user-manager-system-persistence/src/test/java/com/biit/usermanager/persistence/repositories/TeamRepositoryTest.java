package com.biit.usermanager.persistence.repositories;


import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.Team;
import com.biit.usermanager.persistence.entities.TeamMember;
import com.biit.usermanager.persistence.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@Test(groups = {"teamRepository"})

public class TeamRepositoryTest extends AbstractTestNGSpringContextTests {

    private static String TEAM_NAME = "TestName";
    private static String TEAM_2_NAME = "TestName2";
    private static String TEAM_3_NAME = "TestName3";
    private static String APPLICATION_NAME = "ApplicationName";
    private static String ORGANIZATION_NAME = "OrganizationName";
    private static String ORGANIZATION_NAME_2 = "OrganizationName2";

    private static String USER_NAME_1 = "TestUser";
    private static String USER_EMAIL_1 = "TestUser@gmail.com";
    private static String NAME_1 = "TestUserName";
    private static String LASTNAME_1 = "TestUserLastname";
    private static String USER_IDCARD_1 = "TestUserIdCard";
    private static String PHONE_1 = "902202122";

    private static String USER_NAME_2 = "TestUser2";
    private static String USER_EMAIL_2 = "TestUser2@gmail.com";
    private static String NAME_2 = "TestUserName2";
    private static String LASTNAME_2 = "TestUserLastname2";
    private static String USER_IDCARD_2 = "TestUserIdCard2";
    private static String PHONE_2 = "903123123";

    private static String USER_NAME_3 = "TestUser3";
    private static String USER_EMAIL_3 = "TestUser3@gmail.com";
    private static String NAME_3 = "TestUserName3";
    private static String LASTNAME_3 = "TestUserLastname3";
    private static String USER_IDCARD_3 = "TestUserIdCard3";
    private static String PHONE_3 = "9031333333";


    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private UserRepository userRepository;

    private Application application;
    private Organization organization;
    private Organization organization2;
    private Team team;
    private Team team2;
    private Team team3;

    private User user;
    private User user2;
    private User user3;

    @BeforeClass
    public void saveApplication() {
        application = new Application(APPLICATION_NAME);
        application = applicationRepository.save(application);
        Assert.assertNotNull(application.getId());
    }

    @BeforeClass
    public void saveOrganization() {
        organization = new Organization(ORGANIZATION_NAME);
        organization = organizationRepository.save(organization);
        Assert.assertNotNull(organization.getId());

        organization2 = new Organization(ORGANIZATION_NAME_2);
        organization2 = organizationRepository.save(organization2);
        Assert.assertNotNull(organization2.getId());
    }

    @BeforeClass
    public void saveUsers() {
        User user = new User();
        user.setUsername(USER_NAME_1);
        user.setName(NAME_1);
        user.setLastname(LASTNAME_1);
        user.setEmail(USER_EMAIL_1);
        user.setIdCard(USER_IDCARD_1);
        user.setPhone(PHONE_1);
        user.expireAccount(true);

        Assert.assertNull(user.getId());
        this.user = userRepository.save(user);
        Assert.assertNotNull(this.user.getId());

        User user2 = new User();
        user2.setUsername(USER_NAME_2);
        user2.setName(NAME_2);
        user2.setLastname(LASTNAME_2);
        user2.setEmail(USER_EMAIL_2);
        user2.setIdCard(USER_IDCARD_2);
        user2.setPhone(PHONE_2);
        user2.expireAccount(true);

        Assert.assertNull(user2.getId());
        this.user2 = userRepository.save(user2);
        Assert.assertNotNull(this.user2.getId());


        User user3 = new User();
        user3.setUsername(USER_NAME_3);
        user3.setName(NAME_3);
        user3.setLastname(LASTNAME_3);
        user3.setEmail(USER_EMAIL_3);
        user3.setIdCard(USER_IDCARD_3);
        user3.setPhone(PHONE_3);
        user3.expireAccount(true);

        Assert.assertNull(user3.getId());
        this.user3 = userRepository.save(user3);
        Assert.assertNotNull(this.user3.getId());
    }

    @Test
    public void saveTeams() {
        Team team = new Team();
        team.setName(TEAM_NAME);
        team.setOrganization(organization);
        this.team = teamRepository.save(team);
        Assert.assertNotNull(team.getId());

        Team team2 = new Team();
        team2.setName(TEAM_2_NAME);
        team2.setOrganization(organization2);
        this.team2 = teamRepository.save(team2);
        Assert.assertNotNull(team2.getId());

        Team team3 = new Team();
        team3.setName(TEAM_3_NAME);
        team3.setOrganization(organization2);
        this.team3 = teamRepository.save(team3);
        Assert.assertNotNull(team3.getId());
    }

    @Test(dependsOnMethods = "saveTeams")
    public void getTeamsByName() {
        Optional<Team> team = teamRepository.findByNameIgnoreCaseAndOrganization(TEAM_NAME, organization);
        Assert.assertTrue(team.isPresent());
        Assert.assertEquals(team.get().getName(), TEAM_NAME);

        Optional<Team> team2 = teamRepository.findByNameIgnoreCaseAndOrganization(TEAM_2_NAME, organization);
        Assert.assertFalse(team2.isPresent());

        Optional<Team> team3 = teamRepository.findByNameIgnoreCaseAndOrganization(TEAM_3_NAME, organization2);
        Assert.assertTrue(team3.isPresent());
    }

    @Test(dependsOnMethods = {"saveTeams", "getTeamsByName"})
    public void addTeamMembers() {
        teamMemberRepository.save(new TeamMember(team.getId(), user.getId(), null));
        teamMemberRepository.save(new TeamMember(team2.getId(), user2.getId(), null));
        teamMemberRepository.save(new TeamMember(team2.getId(), user3.getId(), null));
        teamMemberRepository.save(new TeamMember(team3.getId(), user3.getId(), null));
    }

    @Test(dependsOnMethods = "addTeamMembers")
    public void getUsersByTeam() {
        final Pageable pageable = PageRequest.of(0, 100);
        List<TeamMember> members = teamMemberRepository.findByIdTeamId(team.getId(), pageable).getContent();
        Assert.assertEquals(members.size(), 1);

        List<TeamMember> members2 = teamMemberRepository.findByIdTeamId(team2.getId(), pageable).getContent();
        Assert.assertEquals(members2.size(), 2);

        List<TeamMember> members3 = teamMemberRepository.findByIdTeamId(team3.getId(), pageable).getContent();
        Assert.assertEquals(members3.size(), 1);
    }

    @Test(dependsOnMethods = "addTeamMembers")
    public void getUsersByOrganization() {
        Set<TeamMember> members = teamMemberRepository.findByOrganizationName(ORGANIZATION_NAME);
        Assert.assertEquals(members.size(), 1);

        Set<TeamMember> members2 = teamMemberRepository.findByOrganizationName(ORGANIZATION_NAME_2);
        //3 memberships but only 2 users.
        Assert.assertEquals(members2.size(), 3);
        Assert.assertEquals(members2.stream().map(member ->
                member.getId().getUserId()).collect(Collectors.toSet()).size(), 2);

        Set<TeamMember> members3 = teamMemberRepository.findByOrganizationName("NOT_EXISTENT");
        Assert.assertEquals(members3.size(), 0);
    }

    @AfterClass(alwaysRun = true)
    public void wipeOut() {
        teamRepository.deleteAll();
        applicationRepository.deleteAll();
        organizationRepository.deleteAll();
        userRepository.deleteAll();
        Assert.assertEquals(applicationRepository.count(), 0);
        Assert.assertEquals(teamRepository.count(), 0);
        Assert.assertEquals(organizationRepository.count(), 0);
        Assert.assertEquals(userRepository.count(), 0);
    }

}
