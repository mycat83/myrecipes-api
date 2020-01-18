package link.myrecipes.api.common;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class RestResource<T> extends Resource<T> {
    public RestResource(T content, String key, Class<?> controllerClass, LinkType[] addLinks, String linkPrefix, Link... links) {
        super(content, links);
        add(linkTo(controllerClass).slash(key).withSelfRel());

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

    public RestResource(T content, Link... links) {
        super(content, links);
    }

    public Link selfLink() {
        return getLinks("self").get(0);
    }
}
