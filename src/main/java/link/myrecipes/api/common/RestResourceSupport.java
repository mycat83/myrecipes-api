package link.myrecipes.api.common;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class RestResourceSupport extends ResourceSupport {

    public RestResourceSupport(Class<?> controllerClass, LinkType[] addLinks, String linkPrefix) {
        super();

        List<LinkType> addLinkList = Arrays.asList(addLinks);
        if (addLinkList.contains(LinkType.CREATE)) {
            add(linkTo(controllerClass).withRel(linkPrefix + "-create"));
        }
        if (addLinkList.contains(LinkType.READ)) {
            add(linkTo(controllerClass).withRel(linkPrefix + "-read"));
        }
        if (addLinkList.contains(LinkType.UPDATE)) {
            add(linkTo(controllerClass).withRel(linkPrefix + "-update"));
        }
        if (addLinkList.contains(LinkType.DELETE)) {
            add(linkTo(controllerClass).withRel(linkPrefix + "-delete"));
        }
        if (addLinkList.contains(LinkType.QUERY)) {
            add(linkTo(controllerClass).withRel(linkPrefix + "-query"));
        }
    }

    public void addProfileLink(String profileLink) {
        add(new Link(profileLink).withRel("profile"));
    }
}
