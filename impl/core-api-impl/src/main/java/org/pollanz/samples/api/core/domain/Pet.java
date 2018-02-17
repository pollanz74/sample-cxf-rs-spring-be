package org.pollanz.samples.api.core.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.pollanz.samples.api.core.pojo.enumeration.Status;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "PET_TB")
@SequenceGenerator(name="pet_sequence",sequenceName="PET_SEQ")
public class Pet implements Serializable {

    @Id
    @GeneratedValue(generator="pet_sequence")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "TAG")
    private String tag;

    @Column(name = "STATUS", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Status status;

}
